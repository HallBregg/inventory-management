package my.group.productscounter.project;

record StageProductResponse(Long productId, Integer position, Integer quantity) {
    static StageProductResponse of(StageProduct stageProduct) {
        return new StageProductResponse(
                stageProduct.getProductId(),
                stageProduct.getPosition().getValue(),
                stageProduct.getQuantity());
    }
}
