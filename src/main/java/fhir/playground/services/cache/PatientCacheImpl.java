package fhir.playground.services.cache;

import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PatientCacheImpl implements PatientCache{

    @CachePut(value = "lastNameCache", key = "#lastName")
    @Override
    public String cacheLastName(String lastName) {
        return "";
    }

    @Cacheable(value = "lastNameCache", key = "#lastName")
    @Override
    public Optional<String> searchLastName(String lastName) {
        return Optional.empty();
    }
}
