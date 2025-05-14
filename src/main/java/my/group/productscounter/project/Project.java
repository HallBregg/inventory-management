package my.group.productscounter.project;

import jakarta.persistence.*;
import my.group.productscounter.BaseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


class StageNotFound extends RuntimeException {
}


@Entity
@Table(name = "projects")
class Project extends BaseEntity {

    @Column(nullable = false)
    private String name;

    @OneToMany(
            mappedBy = "project",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private final List<Stage> stages = new ArrayList<>();

    protected Project() {
    }  // JPA requirement

    Project(String name) {
        this.name = name;
    }

    String getName() {
        return name;
    }

    void setName(String name) {
        this.name = name;
    }

    List<Stage> getStages() {
        return stages;
    }

    Stage getStage(UUID stageId) {
        return stages
                .stream()
                .filter(stage -> stage.getId().equals(stageId))
                .findFirst()
                .orElseThrow(StageNotFound::new);
    }

    Stage addStage(String stageName) {
        Stage stage = new Stage(stageName, this);
        stages.add(stage);
        return stage;
    }

    void deleteStage(UUID stageId) {
        stages.removeIf(stage -> stage.getId() == stageId);
    }
}
