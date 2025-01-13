package fhir.playground.services.cache;

import java.util.Optional;

public interface PatientCache {
    String cacheLastName(String lastName);
    Optional<String> searchLastName(String lastName);

}
