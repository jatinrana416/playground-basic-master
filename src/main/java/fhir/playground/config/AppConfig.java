package fhir.playground.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "app")
public class AppConfig {
    private String baseUrl;
    private String sleepTime;
    private String patientFilePath;
    private boolean inMemoryCacheEnable;

    public String getPatientFilePath() {
        return patientFilePath;
    }

    public void setPatientFilePath(String patientFilePath) {
        this.patientFilePath = patientFilePath;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public String getSleepTime() {
        return sleepTime;
    }

    public void setSleepTime(String sleepTime) {
        this.sleepTime = sleepTime;
    }


    public boolean getInMemoryCacheEnable() {
        return inMemoryCacheEnable;
    }

    public void setInMemoryCacheEnable(boolean inMemoryCacheEnable) {
        this.inMemoryCacheEnable = inMemoryCacheEnable;
    }
}
