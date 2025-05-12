package my.group.productscounter.project;


import jakarta.persistence.*;


@Entity
@Table(
    uniqueConstraints=@UniqueConstraint(columnNames={"stage_id","position"})
)
class StageProduct {

    @EmbeddedId
    private StageProductId id;

    @MapsId("stageId")
    @ManyToOne(optional=false)
    private Stage stage;

    @Column(nullable=false)
    private Long productId;

    @Column(nullable=false)
    private int quantity;

    protected StageProduct() {}  // JPA requirement
}
