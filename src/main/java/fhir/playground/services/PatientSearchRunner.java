package fhir.playground.services;

import java.util.List;

public interface PatientSearchRunner {
    void runSearch(List<String> lastNames);
}
