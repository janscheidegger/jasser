package ch.jasser.boundry.payload;

public class ErrorPayload implements Payload {

    private String errorMessage;

    public ErrorPayload(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
