package com.example.houseprice.services.impls;

import com.example.houseprice.dto.HousePricesCSVReadingDto;
import com.example.houseprice.dto.request.HouseSearchCriteria;
import com.example.houseprice.dto.response.ServiceResponse;
import com.example.houseprice.es.document.HousePricesEsInfo;
import com.example.houseprice.es.service.HousePricesESService;
import com.example.houseprice.exceptions.GenericException;
import com.example.houseprice.models.*;
import com.example.houseprice.repository.*;
import com.example.houseprice.services.HousePricesService;
import com.example.houseprice.specifications.HousePricesSearchSpecifications;
import com.example.houseprice.utils.CSVHelper;
import com.example.houseprice.utils.Defs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Service
@Transactional
public class HousePricesServiceImpl implements HousePricesService {

    Logger logger = LoggerFactory.getLogger(HousePricesServiceImpl.class);

    private final HousePricesRepository housePricesRepository;
    private final LookupCityCodeRepository lookupCityCodeRepository;
    private final LookupNeighbourhoodCityRepository lookupNeighbourhoodCityRepository;
    private final LookupPropertyTypeRepository lookupPropertyTypeRepository;
    private final LookupZipcodeRepository lookupZipcodeRepository;
    private final PgToEsRepository pgToEsRepository;
    private final  HousePricesESService housePricesESService;

    private final LookupBedTypeRepository lookupBedTypeRepository;
    private final LookupCalcellationTypeTypeRepository  lookupCalcellationTypeTypeRepository;
    private final LookupRoomTypeRepository lookupRoomTypeRepository;

    @Autowired
    public HousePricesServiceImpl(
            HousePricesRepository housePricesRepository,
            LookupCityCodeRepository lookupCityCodeRepository,
            LookupNeighbourhoodCityRepository lookupNeighbourhoodCityRepository,
            LookupPropertyTypeRepository lookupPropertyTypeRepository,
            LookupZipcodeRepository lookupZipcodeRepository,
            PgToEsRepository pgToEsRepository,
            HousePricesESService housePricesESService,
            LookupBedTypeRepository lookupBedTypeRepository,
            LookupCalcellationTypeTypeRepository  lookupCalcellationTypeTypeRepository,
            LookupRoomTypeRepository lookupRoomTypeRepository

    ){
        this.housePricesRepository = housePricesRepository;
        this.lookupCityCodeRepository = lookupCityCodeRepository;
        this.lookupNeighbourhoodCityRepository = lookupNeighbourhoodCityRepository;
        this.lookupPropertyTypeRepository = lookupPropertyTypeRepository;
        this.lookupZipcodeRepository = lookupZipcodeRepository;
        this.pgToEsRepository = pgToEsRepository;
        this.housePricesESService = housePricesESService;
        this.lookupBedTypeRepository = lookupBedTypeRepository;
        this.lookupCalcellationTypeTypeRepository = lookupCalcellationTypeTypeRepository;
        this.lookupRoomTypeRepository = lookupRoomTypeRepository;
    }

    @Async
    @Override
    public CompletableFuture<Iterable<HousePrices>> saveHousePricesDataAsync(String file) throws GenericException{
        try (InputStream inputStream = Files.newInputStream(Paths.get(file))) {
            long start = System.currentTimeMillis();
            List<HousePricesCSVReadingDto> housePricesCSVReadingDtoList = CSVHelper.csvToHousePrices(inputStream);
            List<HousePrices> housePricesList = getCSVToModelList(housePricesCSVReadingDtoList);

            Iterable<HousePrices> housePrices = housePricesRepository.saveAll(housePricesList);

            //save a copy of it's into elasticsearch
            saveHousePriceTextualDataIntoES(housePrices);

            long end = System.currentTimeMillis();
            logger.info("Total time: "+(end-start));
            return CompletableFuture.completedFuture(housePrices);

        } catch (IOException e) {
            logger.error("Error occurred while saving house prices data from csv file, message: {}", e.getMessage());
            throw new RuntimeException("fail to store csv data: " + e.getMessage());
        }
    }

    @Async
    @Override
    public CompletableFuture<Iterable<HousePrices>> findAllHousePricesDataAsync() throws GenericException{
        try {
            logger.info("Get list of house prices data by {}", Thread.currentThread().getName());

            Iterable<HousePrices> housePricesList = housePricesRepository.findAll();

            return CompletableFuture.completedFuture(housePricesList);

        } catch (Exception e) {
            logger.error("Error occurred while fetching house prices data from postgres database, message: {}", e.getMessage());
            throw new RuntimeException("fail to store csv data: " + e.getMessage());
        }
    }

    List<HousePrices> getCSVToModelList(List<HousePricesCSVReadingDto> housePricesCSVReadingDtoList) throws GenericException{
        List<HousePrices> list = new ArrayList<>();

        for(HousePricesCSVReadingDto housePricesCSVReadingDto: housePricesCSVReadingDtoList){
            HousePrices housePrices = getModel(housePricesCSVReadingDto);
            list.add(housePrices);
        }
        return list;
    }


    public void save(MultipartFile file) throws GenericException{
        try {
            List<HousePricesCSVReadingDto> housePricesCSVReadingDtoList = CSVHelper.csvToHousePrices(file.getInputStream());

            List<HousePrices> housePricesList = getCSVToModelList(housePricesCSVReadingDtoList);

            housePricesRepository.saveAll(housePricesList);
        } catch (IOException e) {
            throw new RuntimeException("fail to store csv data: " + e.getMessage());
        }
    }
    public void saveHousePriceTextualDataIntoES(Iterable<HousePrices> housePrices){
        //TODO this temp table will be used while syncing data through kafka from pg to es
        List<PgToESTempTable>  pgToESTempTables = new ArrayList<>();

        Iterator iterator = housePrices.iterator();
        while (iterator.hasNext()){
            HousePrices prices = (HousePrices) iterator.next();
            logger.info("Saving data into es id:{}", prices.getId());
            housePricesESService.save(new HousePricesEsInfo(prices));
        }
    }
    public void saveFromFilePath(String file) throws GenericException{
        try (InputStream inputStream = Files.newInputStream(Paths.get(file))) {

            List<HousePricesCSVReadingDto> housePricesCSVReadingDtoList = CSVHelper.csvToHousePrices(inputStream);
            List<HousePrices> housePricesList = getCSVToModelList(housePricesCSVReadingDtoList);

            Iterable<HousePrices> housePrices = housePricesRepository.saveAll(housePricesList);
            saveHousePriceTextualDataIntoES(housePrices);
        } catch (IOException e) {
            logger.error("Error occurred while saving house prices data from csv file, message: {}", e.getMessage());
            throw new RuntimeException("fail to store csv data: " + e.getMessage());
        }
    }

    @Override
    public ResponseEntity<ServiceResponse> insertHousePricesFromKaggleDataset(MultipartFile file) throws GenericException {
        String message = "";
        if (CSVHelper.hasCSVFormat(file)) {
            try {
                this.save(file);

                message = "Uploaded the file successfully: " + file.getOriginalFilename();
                return ResponseEntity.status(HttpStatus.OK).body(new ServiceResponse(null, message));
            } catch (Exception e) {
                message = "Could not upload the file: " + file.getOriginalFilename() + "!";
                return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ServiceResponse(null, message));
            }
        }

        message = "Please upload a csv file!";
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ServiceResponse(null, message));
    }
    @Override
    public ResponseEntity<ServiceResponse> insertHousePricesFromKDFilePath(String filePath) throws GenericException {
        String message = "";
        if(!StringUtils.isEmpty(filePath)){
            try {
                this.saveFromFilePath(filePath);

                message = "Uploaded the file successfully: " + filePath;
                return ResponseEntity.status(HttpStatus.OK).body(new ServiceResponse(null, message));
            } catch (Exception e) {
                logger.error("Error occurred while uploading file!, message: "+e.getMessage());
                e.printStackTrace();
                message = "Could not upload the file: " + filePath + "!";
                return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ServiceResponse(null, message));
            }
        }

        message = "Please upload a csv file!";
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ServiceResponse(null, message));
    }

    @Override
    public Page<HousePrices> getHousePricesList(HouseSearchCriteria criteria, @PageableDefault(value = 10) Pageable pageable) throws GenericException{
        //TODO need to add more search criteria
        Long start = System.currentTimeMillis();
        logger.info("Search start, time: {}", start);
        Page<HousePrices> userPage = housePricesRepository.findAll(
                HousePricesSearchSpecifications.withId(criteria.getId()==null ? criteria.getId() : criteria.getId())
                        .and(HousePricesSearchSpecifications.withAccommodates(criteria.getAccommodates()))
                        .and(HousePricesSearchSpecifications.withBedType(criteria.getBedType()))
                        .and(HousePricesSearchSpecifications.withBathrooms(criteria.getBathrooms()))
                        .and(HousePricesSearchSpecifications.withBedRooms(criteria.getBedrooms()))
                ,pageable
        );

        Long end = System.currentTimeMillis();
        logger.info("Search end, time: {}", end);

        logger.info("Search total, time: {} ms", (end-start));

        return userPage;
    }
    private HousePrices getModel(HousePricesCSVReadingDto csvRecord) throws GenericException{
        HousePrices housePrices = new HousePrices();

        String amenities = csvRecord.getAmenities();
        if(!StringUtils.isEmpty(amenities)){
            housePrices.setAmenities(amenities);
        }

        housePrices.setAccommodates(Integer.parseInt(csvRecord.getAccommodates()));
        housePrices.setBathrooms(StringUtils.isEmpty(csvRecord.getBathrooms())? null: Double.parseDouble(csvRecord.getBathrooms()));

        housePrices.setBedType(getOldIdOrSaveLookupBedType(csvRecord.getBedType()));

        housePrices.setBedrooms(StringUtils.isEmpty(csvRecord.getBedrooms())?null:Integer.parseInt(csvRecord.getBedrooms()));
        housePrices.setBeds(StringUtils.isEmpty(csvRecord.getBeds())?null: Integer.parseInt(csvRecord.getBeds()));

        housePrices.setCancellationPolicy(getOldIdOrSaveLookupCancellationType(csvRecord.getCancellationPolicy()));

        String cityCode = csvRecord.getCity();
        housePrices.setCityCode(getOldIdOrSaveLookupCityCode(cityCode));

        housePrices.setCleaningFee(csvRecord.getCleaningFee().equals("t") ? 1 : 0);
        housePrices.setDescription(csvRecord.getDescription());

        housePrices.setFirstReview(StringUtils.isEmpty(csvRecord.getFirstReview())?null:
                getLocalDateFromFormat(csvRecord.getFirstReview()));

        housePrices.setHostHasProfilePic(csvRecord.getHostHasProfilePic().equals("t")?1:0);
        housePrices.setHostIdentityVerified(csvRecord.getHostIdentityVerified().equals("t")?1:0);
        String responseRate = csvRecord.getHostResponseRatePercent();

        housePrices.setHostResponseRatePercent(StringUtils.isEmpty(responseRate) ? null : Double.parseDouble(responseRate.substring(0, responseRate.length()-1)));
        housePrices.setHostSince(StringUtils.isEmpty(csvRecord.getHostSince())?null:getLocalDateFromFormat(csvRecord.getHostSince()));
        housePrices.setInstantBookable(csvRecord.getInstantBookable().equals("y")? 1: 0);
        housePrices.setLastReview(StringUtils.isEmpty(csvRecord.getLastReview())?null:getLocalDateFromFormat(csvRecord.getLastReview()));
        housePrices.setLatitude(StringUtils.isEmpty(csvRecord.getLatitude())?null : Double.parseDouble(csvRecord.getLatitude()));
        housePrices.setLongitude(StringUtils.isEmpty(csvRecord.getLongitude())?null : Double.parseDouble(csvRecord.getLongitude()));
        housePrices.setName(csvRecord.getName());

        String neighbourhoodCity = csvRecord.getNeighbourhood();
        housePrices.setNeighbourhoodCity(getOldIdOrSaveLookupNeighbourhoodCity(neighbourhoodCity));

        housePrices.setNumberOfReview(StringUtils.isEmpty(csvRecord.getNumberOfReview())?null:Integer.parseInt(csvRecord.getNumberOfReview()));

        String propertyType = csvRecord.getPropertyType();
        housePrices.setPropertyType(getOldIdOrSaveLookupPropertyType(propertyType));

        housePrices.setReviewScoresRating(StringUtils.isEmpty(csvRecord.getReviewScoresRating())?null : Integer.parseInt(csvRecord.getReviewScoresRating()));

        housePrices.setRoomType(getOldIdOrSaveLookupRoomType(csvRecord.getRoomType()));

        housePrices.setThumbnailUrl(csvRecord.getThumbnailUrl());

        String zipCode = csvRecord.getLookupZipcode();
        housePrices.setZipcode(getOldIdOrSaveLookupZipcode(zipCode));

        housePrices.setYNumberOfPersonsWant(Integer.parseInt(csvRecord.getYNumberOfPersonsWant()));
        logger.info("House Prices: "+housePrices.toString());

        return housePrices;
    }
    LocalDate getLocalDateFromFormat(String date){
        String str[] = date.split("/");
        return LocalDate.of(Integer.parseInt(str[2]), Integer.parseInt(str[0]), Integer.parseInt(str[1]));
    }

    public CityCode getOldIdOrSaveLookupCityCode(String code) throws GenericException {
        if(StringUtils.isEmpty(code))return null;
        try{
            Optional<CityCode> optional = lookupCityCodeRepository.getByCode(code.trim());
            if(optional.isPresent()){
                return optional.get();
            }
            return lookupCityCodeRepository.save(new CityCode(code.trim()));

        }catch (Exception e){
            throw new GenericException(e.getMessage());
        }
    }
    public NeighbourhoodCity getOldIdOrSaveLookupNeighbourhoodCity(String name) throws GenericException {
        if(StringUtils.isEmpty(name))return null;
        try{
            Optional<NeighbourhoodCity> optional = lookupNeighbourhoodCityRepository.findByName(name.trim());
            if(optional.isPresent()){
                return optional.get();
            }
            return lookupNeighbourhoodCityRepository.save(new NeighbourhoodCity(name.trim()));

        }catch (Exception e){
            throw new GenericException(e.getMessage());
        }
    }
    public PropertyType getOldIdOrSaveLookupPropertyType(String propertyName) throws GenericException {
        if(StringUtils.isEmpty(propertyName))return null;
        try{
            Optional<PropertyType> optional = lookupPropertyTypeRepository.findByName(propertyName.trim());
            if(optional.isPresent()){
                return optional.get();
            }
            return lookupPropertyTypeRepository.save(new PropertyType(propertyName.trim()));

        }catch (Exception e){
            throw new GenericException(e.getMessage());
        }
    }
    public Zipcode getOldIdOrSaveLookupZipcode(String code) throws GenericException {
        if(StringUtils.isEmpty(code))return null;
        try{
            Optional<Zipcode> optional = lookupZipcodeRepository.findByCode(code.trim());
            if(optional.isPresent()){
                return optional.get();
            }
            return lookupZipcodeRepository.save(new Zipcode(code.trim()));

        }catch (Exception e){
            throw new GenericException(e.getMessage());
        }
    }
    public RoomType getOldIdOrSaveLookupRoomType(String type) throws GenericException {
        if(StringUtils.isEmpty(type))return null;
        try{
            Optional<RoomType> optional = lookupRoomTypeRepository.findByType(type.trim());
            if(optional.isPresent()){
                return optional.get();
            }
            return lookupRoomTypeRepository.save(new RoomType(type.trim()));

        }catch (Exception e){
            throw new GenericException(e.getMessage());
        }
    }
    public CancellationType getOldIdOrSaveLookupCancellationType(String code) throws GenericException {
        if(StringUtils.isEmpty(code))return null;
        try{
            Optional<CancellationType> optional = lookupCalcellationTypeTypeRepository.findByType(code.trim());
            if(optional.isPresent()){
                return optional.get();
            }
            return lookupCalcellationTypeTypeRepository.save(new CancellationType(code.trim()));

        }catch (Exception e){
            throw new GenericException(e.getMessage());
        }
    }
    public BedType getOldIdOrSaveLookupBedType(String code) throws GenericException {
        if(StringUtils.isEmpty(code))return null;
        try{
            Optional<BedType> optional = lookupBedTypeRepository.findByType(code.trim());
            if(optional.isPresent()){
                return optional.get();
            }
            return lookupBedTypeRepository.save(new BedType(code.trim()));

        }catch (Exception e){
            throw new GenericException(e.getMessage());
        }
    }

}
