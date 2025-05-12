package my.group.productscounter.project;


import jakarta.persistence.Embeddable;


@Embeddable
class StageProductId {
    private Long stageId;
    private Position position;

    protected StageProductId() {}  // JPA requirement

    public StageProductId(Long stageId, Position position) {
        this.stageId = stageId;
        this.position = position;
    }

    Long getStageId() { return stageId; }
    Position getPosition() { return position; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof StageProductId that)) return false;
        return stageId.equals(that.getStageId()) && position == that.getPosition();
    }
}
