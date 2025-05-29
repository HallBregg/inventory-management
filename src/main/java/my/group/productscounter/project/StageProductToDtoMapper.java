package my.group.productscounter.project;

import my.group.productscounter.project.dto.StageProductDto;

import java.util.List;

class StageProductToDtoMapper {
    static StageProductDto of(StageProduct product) {
        return new StageProductDto(product.getProductId(), product.getPosition().intValue(), product.getQuantity());
    }

    static List<StageProductDto> of(List<StageProduct> products) {
        return products.stream().map(StageProductToDtoMapper::of).toList();
    }
}
