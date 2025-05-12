package my.group.productscounter.project;

import java.util.List;
import java.util.UUID;


record ProjectResponse(UUID id, String name, List<StageResponse> stages){};
