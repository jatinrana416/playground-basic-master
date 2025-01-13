package fhir.playground.services;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import fhir.playground.config.AppConfig;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.cache.CacheConfig;
import org.apache.http.impl.client.cache.CachingHttpClientBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

@Service
public class FhirClientFactory {

    private final AppConfig appConfig;

    public FhirClientFactory(AppConfig appConfig) {
        this.appConfig = appConfig;
    }

    @Bean
    public IGenericClient createFhirClient() {
        CacheConfig cacheConfig = CacheConfig.custom()
                .setMaxCacheEntries(100)
                .setSharedCache(true)
                .build();
        
        HttpClient cachingHttpClient = CachingHttpClientBuilder.create()
                .setCacheConfig(cacheConfig)
                .build();

        FhirContext fhirContext = FhirContext.forR4();
        fhirContext.getRestfulClientFactory().setHttpClient(cachingHttpClient);

        return fhirContext.newRestfulGenericClient(appConfig.getBaseUrl());
    }
}
