package my.group.productscounter.project.exception;


public class ProductNotFound extends ProjectServiceException {
    private static final String CODE = "PRODUCT_NOT_FOUND";

    public ProductNotFound(Long productId) {
        super(CODE, String.format("Product with id %s does not exist.", productId));
    }
}
