package fhir.playground;

import fhir.playground.config.AppConfig;
import fhir.playground.services.PatientSearchRunnerImpl;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cache.annotation.EnableCaching;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;



@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@EnableCaching
public class Client implements CommandLineRunner {

    private final PatientSearchRunnerImpl searchRunner;
    private final AppConfig appConfig;

    public Client(PatientSearchRunnerImpl searchRunner, AppConfig appConfig) {
        this.searchRunner = searchRunner;
        this.appConfig = appConfig;
    }

    public static void main(String[] args) {
        SpringApplication.run(Client.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        searchRunner.runSearch(getLastNames());
    }

    private List<String> getLastNames() throws IOException {
        List<String> lastNames = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(appConfig.getPatientFilePath()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lastNames.add(line.trim());
            }
        }
        return lastNames;
    }
}
