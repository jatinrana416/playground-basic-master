package fhir.playground.services;

import ca.uhn.fhir.rest.client.api.IClientInterceptor;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.client.api.IHttpRequest;
import ca.uhn.fhir.rest.client.api.IHttpResponse;
import ca.uhn.fhir.util.StopWatch;
import fhir.playground.config.AppConfig;
import fhir.playground.exception.PatientSearchException;
import fhir.playground.services.cache.PatientCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PatientSearchRunnerImpl implements PatientSearchRunner {

    private final PatientSearchService searchService;
    private final IGenericClient client;
    private final PatientCache patientCache;
    private final AppConfig appConfig;
    boolean waitAfterExecution;
    boolean isResultForPatient = false;
    private long totalResponseTime = 0;
    private int totalCount = 0;


    public PatientSearchRunnerImpl(IGenericClient client, PatientSearchService searchService, PatientCache patientCache, AppConfig appConfig, boolean waitAfterExecution) {
        this.client = client;
        this.searchService = searchService;
        this.patientCache = patientCache;
        this.appConfig = appConfig;
        this.waitAfterExecution = waitAfterExecution;
    }

    @Autowired
    public PatientSearchRunnerImpl(IGenericClient client, PatientSearchService searchService, AppConfig appConfig, PatientCache patientCache) {
        this(client, searchService, patientCache, appConfig, true);
    }

    @Override
    public void runSearch(List<String> lastNames) {
        ClientInterceptorImpl clientInterceptor = new ClientInterceptorImpl();
        client.registerInterceptor(clientInterceptor);


        executePatientsSearch(lastNames, true);
        executePatientsSearch(lastNames, false);
        executePatientsSearch(lastNames, true);

        client.unregisterInterceptor(clientInterceptor);
    }

    public class ClientInterceptorImpl implements IClientInterceptor {
        String patientName = "";

        @Override
        public void interceptRequest(IHttpRequest iHttpRequest) {
            if (iHttpRequest.getUri().contains("Patient")) {
                patientName = iHttpRequest.getUri().split("=")[1];
                isResultForPatient = true;
            } else {
                isResultForPatient = false;
            }
            iHttpRequest.addHeader("Accept", "application/fhir+xml");
        }

        @Override
        public void interceptResponse(IHttpResponse iHttpResponse) {
            boolean fromCache = false;
            StopWatch stopWatch = iHttpResponse.getRequestStopWatch();

            List<String> cacheHeader = iHttpResponse.getHeaders("x-Cache");
            if (!cacheHeader.isEmpty() && cacheHeader.get(0).contains("HIT")) {
                fromCache = true;
            }
            if (isResultForPatient) {
                if (fromCache) {
                    System.out.printf("Cache Hit For Patient Name: %s \n", patientName);
                } else {
                    System.out.printf("Cache Miss For Patient Name: %s \n", patientName);
                }
            }


            if (stopWatch != null && isResultForPatient) {
                long responseTime = stopWatch.getMillisAndRestart();
                totalResponseTime += responseTime;
                totalCount++;
                patientCache.cacheLastName(patientName);
            }
        }
    }

    private void executePatientsSearch(List<String> lastNames, boolean isCacheDisabled) {
        resetCount();
        try {
            for (String lastName : lastNames) {
                searchPatientName(isCacheDisabled, lastName);
            }
            double averageResponseTime = totalCount == 0 ? 0 : (double) totalResponseTime / totalCount;
            System.out.println("Average Response Time for this loop: " + averageResponseTime + " ms");
            sleep();
        } catch (Exception e) {
            throw new PatientSearchException("Error occurred during patient search %s", e);
        }

    }

    private void searchPatientName(boolean isCacheDisabled, String lastName) {
        long startTime = System.currentTimeMillis();
        if(!isCacheDisabled && appConfig.getInMemoryCacheEnable() && patientCache.searchLastName(lastName).isPresent()){
            System.out.printf("Cache Hit For Patient Name: %s \n", lastName);
            long endTime = System.currentTimeMillis();
            totalCount++;
            totalResponseTime +=  endTime - startTime;

        }
        else {
            searchService.searchPatientsByLastName(lastName, isCacheDisabled);
        }
    }

    private void resetCount() {
        totalCount = 0;
        totalResponseTime = 0L;
    }

    private void sleep() {
        if (waitAfterExecution) {
            try {
                Thread.sleep(Long.parseLong(appConfig.getSleepTime()));
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }


}
