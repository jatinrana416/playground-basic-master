package fhir.playground.services;

import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.client.api.IHttpRequest;
import ca.uhn.fhir.rest.client.api.IHttpResponse;
import fhir.playground.config.AppConfig;
import fhir.playground.services.cache.PatientCache;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;

class PatientSearchRunnerTest {

    @Mock
    private IGenericClient mockClient;

    @Mock
    private PatientSearchService mockSearchService;

    @Mock
    private PatientCache patientCache;

    @Mock
    private AppConfig appConfig;

    private PatientSearchRunnerImpl.ClientInterceptorImpl clientInterceptor;
    private PatientSearchRunnerImpl runner;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        runner = new PatientSearchRunnerImpl(mockClient, mockSearchService, patientCache, appConfig,false);
        clientInterceptor = runner.new ClientInterceptorImpl();
    }

    @Test
    void verifyHeadersWhenSendingRequest() {
        IHttpRequest mockRequest = mock(IHttpRequest.class);
        when(mockRequest.getUri()).thenReturn("BASE_URL");
        clientInterceptor.interceptRequest(mockRequest);

        verify(mockRequest).addHeader("Accept", "application/fhir+xml");
    }

    @Test
    void verifyInterceptResponseShouldBeACacheHit() {
        IHttpRequest mockRequest = mock(IHttpRequest.class);
        IHttpResponse mockResponse = mock(IHttpResponse.class);
        when(mockRequest.getUri()).thenReturn("BASE_URL");
        when(mockResponse.getHeaders("x-Cache")).thenReturn(Collections.singletonList("HIT"));

        clientInterceptor.interceptResponse(mockResponse);
        verify(mockResponse).getHeaders("x-Cache");
    }

    @Test
    void searchPatient() {
        List<String> lastNames = List.of("Smith");
        doNothing().when(mockSearchService).searchPatientsByLastName(anyString(), anyBoolean());
        when(appConfig.getInMemoryCacheEnable()).thenReturn(false);
        runner.runSearch(lastNames);

        verify(mockSearchService,  times(3)).searchPatientsByLastName(anyString(), anyBoolean());

    }

}
