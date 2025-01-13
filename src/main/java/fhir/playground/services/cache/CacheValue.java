package fhir.playground.services.cache;

public class CacheValue {
    private final String lastName;
    private final long responseTime;

    public CacheValue(String lastName, long responseTime) {
        this.lastName = lastName;
        this.responseTime = responseTime;
    }

    public String getLastName() {
        return lastName;
    }

    public long getResponseTime() {
        return responseTime;
    }

    @Override
    public String toString() {
        return "CacheValue{" +
                "lastName='" + lastName + '\'' +
                ", responseTime=" + responseTime +
                '}';
    }
}
