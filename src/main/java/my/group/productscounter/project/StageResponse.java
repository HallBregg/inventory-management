package my.group.productscounter.project;

import java.util.List;
import java.util.UUID;


record StageResponse(UUID id, String name, List<StageProductResponse> products){
    static StageResponse of(Stage stage) {
        return new StageResponse(
                stage.getId(),
                stage.getName(),
                stage
                        .getProducts()
                        .stream()
                        .map(StageProductResponse::of)
                        .toList());
    }
};
