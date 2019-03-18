package no.ruter.ps.servicelinkDistanceService.servicelinkService;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import no.ruter.ps.servicelinkDistanceService.DomainModel.Servicelink;
import no.ruter.ps.servicelinkDistanceService.DomainModel.Servicelinkj;
import no.ruter.ps.servicelinkDistanceService.DomainModel.Servicelinks;
import org.elasticsearch.action.admin.indices.mapping.get.GetFieldMappingsRequest;
import org.elasticsearch.action.admin.indices.mapping.get.GetFieldMappingsResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
@AllArgsConstructor
public class ServicelinkDistanceService {
    private RestHighLevelClient restHighLevelClient;
    private ObjectMapper objectMapper;

    @SneakyThrows
    public String getServicelinkById(String id) {
        log.info("Input data: {}", id);
        GetRequest getRequest = new GetRequest("servicelink_journey", "servicelinkj",id);
        GetResponse getResponse = restHighLevelClient.get(getRequest, RequestOptions.DEFAULT);
        Map<String, Object> resultMap = getResponse.getSource();
        Servicelinks servicelinks = objectMapper.convertValue(getResponse.getSource(), Servicelinks.class);
        getResponse.getField("servicelinks");
        log.info("the response object : {}", resultMap.get("journeyPatternID"));
        log.info("the response object : {}", servicelinks.getServicelinks().get(0).getDistance());
        return (String)resultMap.get("journeyPatternID");
    }
    @SneakyThrows
    public void getAll(){
        GetRequest getRequest = new GetRequest("servicelink_journey","servicelinkj","b335e335-ed32-43bc-9b0c-f47c6844f582");
        GetResponse getResponse = restHighLevelClient.get(getRequest,RequestOptions.DEFAULT);
        log.info("##### response: {}", getResponse);
    }
    @SneakyThrows
    public void getByServiceLine(){
        GetFieldMappingsRequest request = new GetFieldMappingsRequest();
        request.indices("servicelink_journey");
        request.fields("servicelinks.distnace","journeyPatternID");
        GetFieldMappingsResponse response = restHighLevelClient.indices().getFieldMapping(request,RequestOptions.DEFAULT);
        log.info("************ response : {}", response);
    }
    @SneakyThrows
    public String getIndexIdByJourneyId(String journeyId){
        GetRequest request = new GetRequest("servicelink_journey","servicelinkj",journeyId);
        GetResponse response = restHighLevelClient.get(request,RequestOptions.DEFAULT);
        log.info("Data: {}",response.getSource().get("journeyPatterID"));
        return (String)response.getSource().get("journeyPatterID");
    }
    @SneakyThrows
    public float calculateTotalDistnaceByJourneyId(String id){
        GetRequest request = new GetRequest("sl","sl",id);
        GetResponse response = restHighLevelClient.get(request,RequestOptions.DEFAULT);
        Servicelinks servicelinks = objectMapper.convertValue(response.getSource(), Servicelinks.class);
        float totalDistance = servicelinks.getServicelinks().stream()
                .map(sl -> sl.getDistance())
                .reduce((d1,d2)->d1+d2).get();
        return totalDistance;
    }
    @SneakyThrows
    public float calculateTotalDistanceByLinePublicCod(String linePublicCode){
        log.info("Line public code : {}", linePublicCode);
        SearchRequest request = new SearchRequest("sl");
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.boolQuery()
            .must(QueryBuilders.matchQuery("routeDto.lineDto.publicCode",linePublicCode)));
        request.source(searchSourceBuilder);
        SearchResponse response = restHighLevelClient.search(request,RequestOptions.DEFAULT);
        log.info("search status: {}", response.status());
        log.info("search response took : {}",response.getTook());

        SearchHits searchHits = response.getHits();
        List<Servicelinkj> listServicelinks = new ArrayList<>();
        for(SearchHit hit : searchHits.getHits()){

            Servicelinkj slj = objectMapper.convertValue(hit.getSourceAsMap(),Servicelinkj.class);
            listServicelinks.add(slj);

        }
        log.info("size of line journey: {}", listServicelinks.size());
        float distance = 0;
        for(Servicelinkj s : listServicelinks){
            for(Servicelink sl : s.getServicelinks()) {

                distance = distance+ sl.getDistance();
            }
            log.info("Data from conversion: {}",distance);
        }
        return distance;
    }

    @SneakyThrows
    public float calcluateTotalDistnaceByJourneyPatternId(String journeyPatterId){
        log.info("JourneyPatterId : {}", journeyPatterId);
        SearchRequest request = new SearchRequest("servicelink_journey");
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.boolQuery()
                .must(QueryBuilders.matchQuery("journeyPatternID",journeyPatterId)));
        //TermsAggregationBuilder aggregationBuilder = AggregationBuilders.terms("by_distnace").field("servicelinks.distnace");
        //aggregationBuilder.subAggregation(AggregationBuilders.sum("servicelinks.distnace"));
        //searchSourceBuilder.aggregation(aggregationBuilder);
        request.source(searchSourceBuilder);

        SearchResponse response = restHighLevelClient.search(request,RequestOptions.DEFAULT);
        RestStatus status = response.status();
        TimeValue took = response.getTook();
        SearchHits hits = response.getHits();
        long totalhits = hits.getTotalHits();
        log.info("total hits after searching : {}", totalhits);
        String JoPAId;
        float totalDistance =0;
        Servicelinks servicelinks = null;

        //Map<String,Object> sls =
        totalDistance=Arrays.stream(response.getHits().getHits())
                //.map(hit -> hit.field("servicelinks"))
                .map(sl -> objectMapper.convertValue(sl.getSourceAsMap(),Servicelinks.class))
                .map(sl ->sl.getServicelinks())
                .flatMap(lsl -> lsl.stream())
                .map(sl -> sl.getDistance())
                .reduce((d1,d2)-> d1+d2).get();
        log.info("distance : {}", totalDistance);
//        for(SearchHit hit : hits.getHits()){
//            servicelinks = objectMapper.convertValue(hit.getSourceAsMap(),Servicelinks.class);
//
//
//            log.info("data {}",servicelinks.getServicelinks().size());
//            log.info("pid : {}", servicelinks.getServicelinks().size());
//            totalDistance = servicelinks.getServicelinks().stream().map(sl -> sl.getDistance())
//                    .reduce((d1,d2)-> d1+d2).get();
//            log.info("Total Distance : {}", totalDistance);
//
//        }
        return totalDistance;
    }

}
