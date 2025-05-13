package my.group.productscounter.project;


import jakarta.persistence.Embeddable;

import java.util.UUID;


@Embeddable
class StageProductId {
    private UUID stageId;
    private Position position;

    protected StageProductId() {
    }  // JPA requirement

    public StageProductId(UUID stageId, Position position) {
        this.stageId = stageId;
        this.position = position;
    }

    UUID getStageId() {
        return stageId;
    }

    Position getPosition() {
        return position;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof StageProductId that)) return false;
        return stageId.equals(that.getStageId()) && position == that.getPosition();
    }
}
