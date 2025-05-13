package my.group.productscounter.project;

import java.util.UUID;

record StageSummaryResponse(UUID id, String name) {
    static StageSummaryResponse of(Stage stage) {
        return new StageSummaryResponse(stage.getId(), stage.getName());
    }
};
