package my.group.productscounter.project.exception;

public abstract class ProjectServiceException extends RuntimeException {
    private String code = "BASE_ERROR";

    protected ProjectServiceException(String code) {
        super();
        this.code = code;
    }

    protected ProjectServiceException(String code, String message) {
        super(message);
        this.code = code;
    }

    protected ProjectServiceException(String code, Throwable cause) {
        super(cause);
        this.code = code;
    }

    protected ProjectServiceException(String code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
