package my.group.productscounter.project;


import jakarta.persistence.*;


@Entity
@Table(
        uniqueConstraints = @UniqueConstraint(columnNames = {"stage_id", "position"})
)
class StageProduct {

    @EmbeddedId
    private StageProductId id;

    @MapsId("stageId")
    @ManyToOne(optional = false)
    private Stage stage;

    @Column(nullable = false)
    private Long productId;

    @Column(nullable = false)
    private int quantity;

    protected StageProduct() {
    }  // JPA requirement

    StageProduct(Stage stage, Long productId, int quantity, Position position){
        this.id = new StageProductId(stage.getId(), position);
        this.stage = stage;
        this.productId = productId;
        this.quantity = quantity;
    }

    Long getProductId() {
        return productId;
    }

    Integer getQuantity() {
        return quantity;
    }

    Position getPosition() {
        return id.getPosition();
    }

    void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
