package fhir.playground.services;

import ca.uhn.fhir.rest.api.CacheControlDirective;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.gclient.IQuery;
import org.hl7.fhir.instance.model.api.IBaseBundle;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Patient;
import org.springframework.stereotype.Service;

@Service
public class PatientSearchServiceImpl implements PatientSearchService{
    private final IGenericClient client;

    public PatientSearchServiceImpl(IGenericClient client) {
        this.client = client;;
    }

    @Override
    public void searchPatientsByLastName(String lastName, boolean isCacheDisabled) {
        IQuery<IBaseBundle> patientQuery = client
                .search()
                .forResource("Patient");

        if(isCacheDisabled){
            disableCaching(patientQuery);
        }

        patientQuery.where(Patient.FAMILY.matches().value(lastName));

        patientQuery
                .returnBundle(Bundle.class)
                .execute();
    }

    private  void disableCaching(IQuery<IBaseBundle> patientQuery) {
            patientQuery.cacheControl( new CacheControlDirective().setNoCache(true));
    }
}
