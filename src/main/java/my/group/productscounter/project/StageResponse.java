package my.group.productscounter.project;

import java.util.List;
import java.util.UUID;


record StageResponse(UUID id, String name, List<StageProductResponse> products){};
