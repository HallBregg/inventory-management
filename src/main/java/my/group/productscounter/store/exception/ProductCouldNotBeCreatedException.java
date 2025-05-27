package my.group.productscounter.store.exception;

public class ProductCouldNotBeCreatedException extends ProductStoreServiceException {
    private static final String CODE = "PRODUCT_CREATE_ERROR";
    private static final String MESSAGE = "Could not create the product.";

    public ProductCouldNotBeCreatedException() {
        super(CODE, MESSAGE);
    }

    public ProductCouldNotBeCreatedException(Throwable cause) {
        super(CODE, MESSAGE, cause);
    }
}
