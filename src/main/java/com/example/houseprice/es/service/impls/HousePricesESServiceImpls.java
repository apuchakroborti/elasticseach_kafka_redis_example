package com.example.houseprice.es.service.impls;

import com.example.houseprice.dto.request.HouseSearchCriteria;
import com.example.houseprice.es.document.HousePricesEsInfo;
import com.example.houseprice.es.enums.EsSearchQueryType;
import com.example.houseprice.es.repository.HousePricesEsRepository;
import com.example.houseprice.es.service.HousePricesESService;
import com.example.houseprice.services.impls.HousePricesServiceImpl;
import com.example.houseprice.utils.Utils;
import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.Criteria;
import org.springframework.data.elasticsearch.core.query.CriteriaQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static org.elasticsearch.index.query.QueryBuilders.boolQuery;

@Service
public class HousePricesESServiceImpls implements HousePricesESService {

    Logger logger = LoggerFactory.getLogger(HousePricesESServiceImpls.class);

    private final HousePricesEsRepository housePricesEsRepository;


    private final ElasticsearchOperations elasticsearchTemplate;

    @Autowired
    public HousePricesESServiceImpls(HousePricesEsRepository housePricesEsRepository,
                                     ElasticsearchOperations elasticsearchTemplate){
        this.housePricesEsRepository = housePricesEsRepository;
        this.elasticsearchTemplate = elasticsearchTemplate;
    }

    @Override
    public void save(final HousePricesEsInfo housePricesEsInfo){
        housePricesEsRepository.save(housePricesEsInfo);
    }

    @Override
    public void saveAll(List<HousePricesEsInfo> housePricesEsInfo){
        try {
            housePricesEsRepository.saveAll(housePricesEsInfo);
            logger.info("Saving data into completed, size: {}", housePricesEsInfo.size());
        }catch (Exception e){
            logger.error("Error occurred while saving data into es, message: {}", e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public HousePricesEsInfo findById(final Long id){
        return housePricesEsRepository.findById(id).orElse(null);
    }

    @Override
    public SearchHits<HousePricesEsInfo> advSearchData(HouseSearchCriteria searchCriteria, Pageable pageable){
        if(searchCriteria.getEsSearchQueryType()!=null && searchCriteria.getEsSearchQueryType().equals(EsSearchQueryType.NativeQuery)){
            //TODO need to add more search criteria

            BoolQueryBuilder queryBuilder = boolQuery();
            if(!Utils.isNull(searchCriteria.getAccommodates())){
                queryBuilder.must(QueryBuilders.matchQuery("accommodates", searchCriteria.getAccommodates()));
            }
            if(!Utils.isNull(searchCriteria.getBedType())){
                queryBuilder.must(QueryBuilders.matchQuery("bed_type", searchCriteria.getBedType()));
            }
            if(!Utils.isNull(searchCriteria.getBedrooms())){
                queryBuilder.must(QueryBuilders.matchQuery("bathrooms", searchCriteria.getBathrooms()));
            }
            if(!Utils.isNullOrEmpty(searchCriteria.getNameOrDescription())){
                queryBuilder.should( QueryBuilders
                        .multiMatchQuery(searchCriteria.getNameOrDescription(), "name", "description")
                        .fuzziness(Fuzziness.AUTO));
            }

            Query searchQuery = new NativeSearchQueryBuilder()
                    .withQuery(queryBuilder)
                    .build();
            searchQuery.setPageable(pageable);

            return elasticsearchTemplate.search(searchQuery, HousePricesEsInfo.class);
        }else if(searchCriteria.getEsSearchQueryType()!=null && searchCriteria.getEsSearchQueryType().equals(EsSearchQueryType.StringQuery)){
            //TODO need to implement
            return null;
        }
        else/*(criteria.getEsSearchQueryType().equals(EsSearchQueryType.CriteriaQuery))*/{
            CriteriaQuery query = buildCriteriaSearchQuery(searchCriteria);
            query.setPageable(pageable);
            return elasticsearchTemplate.search(query, HousePricesEsInfo.class);
        }
    }
    private CriteriaQuery buildCriteriaSearchQuery(HouseSearchCriteria searchCriteria) {
        //TODO need to add more search criteria
        Criteria criteria = new Criteria();

        if (!Utils.isNull(searchCriteria.getId())) {
            criteria.and(new Criteria("id").is(searchCriteria.getId()));
        }
        if (!Utils.isNull(searchCriteria.getAccommodates())) {
            criteria.and(new Criteria("accommodates").is(searchCriteria.getAccommodates()));
        }
        if(!Utils.isNull(searchCriteria.getBedType())){
            criteria.and(new Criteria("bed_type").is(searchCriteria.getBedType()));
        }
        if (!Utils.isNull(searchCriteria.getBedrooms())) {
            criteria.and(new Criteria("bedrooms").is(searchCriteria.getBedrooms()));
        }
        if (!Utils.isNull(searchCriteria.getBathrooms())) {
            criteria.and(new Criteria("bathrooms").is(searchCriteria.getBathrooms()));
        }
        if(!Utils.isNullOrEmpty(searchCriteria.getNameOrDescription())){
            criteria.or( new Criteria("name").fuzzy(searchCriteria.getNameOrDescription()));
            criteria.or( new Criteria("description").fuzzy(searchCriteria.getNameOrDescription()));
        }

        return new CriteriaQuery(criteria);
    }
    @Override
    public void deleteAll(){
        housePricesEsRepository.deleteAll();
    }

}
