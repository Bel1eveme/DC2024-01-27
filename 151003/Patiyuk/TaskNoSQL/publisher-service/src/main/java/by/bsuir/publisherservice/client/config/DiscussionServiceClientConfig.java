package by.bsuir.publisherservice.client.config;

import by.bsuir.publisherservice.client.DiscussionServiceClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.support.RestTemplateAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;
import org.springframework.web.util.DefaultUriBuilderFactory;

@Configuration
public class DiscussionServiceClientConfig {

    @Bean
    public DiscussionServiceClient discussionServiceClient() {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setUriTemplateHandler(new DefaultUriBuilderFactory("http://localhost:24130/api/v1.0"));
        HttpServiceProxyFactory httpServiceProxyFactory = HttpServiceProxyFactory
                .builderFor(RestTemplateAdapter.create(restTemplate))
                .build();
        return httpServiceProxyFactory.createClient(DiscussionServiceClient.class);
    }
}
