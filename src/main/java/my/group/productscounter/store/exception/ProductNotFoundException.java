package my.group.productscounter.store.exception;

public class ProductNotFoundException extends ProductStoreServiceException {
    private static final String CODE = "PRODUCT_NOT_FOUND";

    public ProductNotFoundException() {
        super(CODE);
    }

    protected ProductNotFoundException(String message) {
        super(CODE, message);
    }

    protected ProductNotFoundException(String message, Throwable cause) {
        super(CODE, message, cause);
    }
}
