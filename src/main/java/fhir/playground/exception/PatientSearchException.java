package fhir.playground.exception;

public class PatientSearchException extends RuntimeException {

    public PatientSearchException(String message) {
        super(message);
    }

    public PatientSearchException(String message, Throwable cause) {
        super(message, cause);
    }
}
