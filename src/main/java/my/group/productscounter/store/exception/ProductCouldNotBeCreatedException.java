package my.group.productscounter.store.exception;

public class ProductCouldNotBeCreatedException extends ProductStoreServiceException {
    private static final String CODE = "PRODUCT_COULD_NOT_BE_CREATED";

    protected ProductCouldNotBeCreatedException() {
        super(CODE);
    }

    protected ProductCouldNotBeCreatedException(String message) {
        super(CODE, message);
    }

    protected ProductCouldNotBeCreatedException(String message, Throwable cause) {
        super(CODE, message, cause);
    }
}
