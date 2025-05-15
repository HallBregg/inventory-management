package my.group.productscounter.store.exception;

public abstract class ProductStoreServiceException extends RuntimeException {
    private String code = "BASE_ERROR";

    protected ProductStoreServiceException(String code) {
        super();
        this.code = code;
    }

    protected ProductStoreServiceException(String code, String message) {
        super(message);
        this.code = code;
    }

    protected ProductStoreServiceException(String code, Throwable cause) {
        super(cause);
        this.code = code;
    }

    protected ProductStoreServiceException(String code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }

    String getCode() {
        return code;
    }
}
