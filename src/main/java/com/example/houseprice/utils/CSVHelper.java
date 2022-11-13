package com.example.houseprice.utils;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import com.example.houseprice.dto.HousePricesCSVReadingDto;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;


public class CSVHelper {

    public static Logger logger = LoggerFactory.getLogger(CSVHelper.class);

    public static String TYPE = "text/csv";

    public static boolean hasCSVFormat(MultipartFile file) {

        if (!TYPE.equals(file.getContentType())) {
            return false;
        }

        return true;
    }

    public static List<HousePricesCSVReadingDto> csvToHousePrices(InputStream is) {
        int count=4000;
        try (BufferedReader fileReader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
             
             CSVParser csvParser = new CSVParser(fileReader,
                     CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim());) {

            List<HousePricesCSVReadingDto> housePricesArrayList = new ArrayList<>();

            Iterable<CSVRecord> csvRecords = csvParser.getRecords();

            for (CSVRecord csvRecord : csvRecords) {
                HousePricesCSVReadingDto housePricesDto = new HousePricesCSVReadingDto();

                String amenities = csvRecord.get("amenities");
                if(!StringUtils.isEmpty(amenities)){
                    amenities = amenities.replace("{", "");
                    amenities = amenities.replace("}", "");
                    amenities = amenities.replace("\"", "");
                    housePricesDto.setAmenities(amenities);
                }else housePricesDto.setAmenities(null);

                housePricesDto.setAccommodates((csvRecord.get("accommodates")));
                housePricesDto.setBathrooms(csvRecord.get("bathrooms"));
                housePricesDto.setBedType(csvRecord.get("bed_type"));
                housePricesDto.setBedrooms((csvRecord.get("bedrooms")));
                housePricesDto.setBeds((csvRecord.get("beds")));
                housePricesDto.setCancellationPolicy(csvRecord.get("cancellation_policy"));
                housePricesDto.setCity(csvRecord.get("city"));
                housePricesDto.setCleaningFee(csvRecord.get("cleaning_fee"));
                housePricesDto.setDescription(csvRecord.get("description"));
                housePricesDto.setFirstReview(csvRecord.get("first_review"));
                housePricesDto.setHostHasProfilePic(csvRecord.get("host_has_profile_pic"));
                housePricesDto.setHostIdentityVerified(csvRecord.get("host_identity_verified"));
                String responseRate = csvRecord.get("host_response_rate");
                housePricesDto.setHostResponseRatePercent((responseRate));
                housePricesDto.setHostSince(csvRecord.get("host_since"));
                housePricesDto.setInstantBookable(csvRecord.get("instant_bookable"));
                housePricesDto.setLastReview(csvRecord.get("last_review"));
                housePricesDto.setLatitude((csvRecord.get("latitude")));
                housePricesDto.setLongitude((csvRecord.get("latitude")));
                housePricesDto.setName(csvRecord.get("name"));
                housePricesDto.setNeighbourhood(csvRecord.get("neighbourhood"));
                housePricesDto.setNumberOfReview((csvRecord.get("number_of_reviews")));
                housePricesDto.setPropertyType(csvRecord.get("property_type"));
                housePricesDto.setReviewScoresRating((csvRecord.get("review_scores_rating")));
                housePricesDto.setRoomType((csvRecord.get("room_type")));
                housePricesDto.setThumbnailUrl(csvRecord.get("thumbnail_url"));
                housePricesDto.setLookupZipcode(csvRecord.get("zipcode"));
                housePricesDto.setYNumberOfPersonsWant((csvRecord.get("y")));

                housePricesArrayList.add(housePricesDto);
                count--;
                if(count<=0)break;
            }

            return housePricesArrayList;
        } catch (IOException e) {
            logger.error("Exception  while reading csv file!, message: "+e.getMessage());
            throw new RuntimeException("fail to parse CSV file: " + e.getMessage());
        }
    }


}
