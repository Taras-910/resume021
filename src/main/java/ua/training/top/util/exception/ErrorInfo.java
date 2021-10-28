package ua.training.top.util.exception;

public class ErrorInfo {
    private final String url;
    private final ErrorType errorType;
    private final String[] details;

    public ErrorInfo(StringBuffer url, ErrorType errorType, String[] details) {
        this.url = url.toString();
        this.errorType = errorType;
        this.details = details;

    }
}
