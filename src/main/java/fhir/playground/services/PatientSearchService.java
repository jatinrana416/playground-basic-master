package fhir.playground.services;

public interface PatientSearchService {
    void searchPatientsByLastName(String lastName, boolean isCacheDisabled);
}
