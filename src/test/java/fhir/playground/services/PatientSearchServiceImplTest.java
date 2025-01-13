package fhir.playground.services;

import ca.uhn.fhir.rest.api.CacheControlDirective;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.gclient.IQuery;
import ca.uhn.fhir.rest.gclient.IUntypedQuery;
import org.hl7.fhir.instance.model.api.IBaseBundle;
import org.hl7.fhir.r4.model.Bundle;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class PatientSearchServiceImplTest {

    @Mock
    private IGenericClient mockClient;

    @Mock
    private IQuery<IBaseBundle> mockQuery;

    @Mock
    private IUntypedQuery<IBaseBundle> mockSearch;

    private PatientSearchServiceImpl service;
    String lastName = "Smith";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        service = new PatientSearchServiceImpl(mockClient);
        when(mockClient.search()).thenReturn(mockSearch);
        when(mockClient.search().forResource("Patient")).thenReturn(mockQuery);
        when(mockQuery.returnBundle(any())).thenReturn(mockQuery);
    }

    @Test
    void searchPatientWithCaching() {

        boolean isCacheDisabled = false;

        service.searchPatientsByLastName(lastName, isCacheDisabled);

        // Assert
        verify(mockClient.search()).forResource("Patient");
        verify(mockQuery, never()).cacheControl(any(CacheControlDirective.class));
        verify(mockQuery).returnBundle(Bundle.class);
        verify(mockQuery).execute();
    }

    @Test
    void searchPatientWithoutCaching() {
        boolean isCacheDisabled = true;

        service.searchPatientsByLastName(lastName, isCacheDisabled);

        verify(mockClient.search()).forResource("Patient");

        ArgumentCaptor<CacheControlDirective> captor = ArgumentCaptor.forClass(CacheControlDirective.class);
        verify(mockQuery).cacheControl(captor.capture());
        verify(mockQuery).returnBundle(Bundle.class);
        verify(mockQuery).execute();

        CacheControlDirective capturedDirective = captor.getValue();
        assert capturedDirective.isNoCache();
    }
}
