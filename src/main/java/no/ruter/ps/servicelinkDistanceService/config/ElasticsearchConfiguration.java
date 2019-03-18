package no.ruter.ps.servicelinkDistanceService.config;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;

@Configuration
public class ElasticsearchConfiguration {

    @Value("${elasticsearch.host}")
    private String esHost;
    @Value("${elasticsearch.port}")
    private String esPort;
    @Value("${elasticsearch.protocole}")
    private String esProtocol;
    @Bean(destroyMethod = "close")
    public RestHighLevelClient client(){
        RestHighLevelClient highLevelClient = new RestHighLevelClient(
                RestClient.builder(new HttpHost(esHost))
        );
        return  highLevelClient;
    }

}

