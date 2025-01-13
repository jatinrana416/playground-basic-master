# HAPI FHIR Playground: 

*  This project is designed to call the hapi fhir search API to get the data for patients wby passing lastName in the API request
*  Used Spring Boot to make use of dependency injection
*  Added test units
*  Using InMemory ehcache for caching
*  The main file is named as Client which is under the fhir.playground package

### PREREQUISITES

*  Java 21
*  Maven

### IMPORTANT PROPERTIES IN APPLICATION PROPERTIES FILE

* app.baseUrl : In this property we can set the HAPI FHIR URL(http://hapi.fhir.org/baseR4)
* app.sleepTime: This property sets the waiting time for the next cycle to search last names
* app.patientFilePath: This property sets the file path in which patients last name are added
* app.inMemoryCacheEnable : If this flag is set to true, then application will be using the inMemory cache. If this flag is set to false then 
  application will be using caching on the server side of hapi.fhir by passing header as no cache
