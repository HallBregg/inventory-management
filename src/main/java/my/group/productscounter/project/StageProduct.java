package my.group.productscounter.project;


import jakarta.persistence.*;
import my.group.productscounter.BaseEntity;

import java.util.Objects;


@Entity
class StageProduct extends BaseEntity {
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, updatable = false)
    private Stage stage;

    @Embedded
    private Position position;

    @Column(nullable = false)
    private Long productId;

    @Column(nullable = false)
    private int quantity;

    protected StageProduct() {
    }  // JPA requirement

    StageProduct(Stage stage, Long productId, Position position, int quantity) {
        this.stage = Objects.requireNonNull(stage);
        this.productId = Objects.requireNonNull(productId);
        this.position = Objects.requireNonNull(position);
        setQuantity(quantity);
    }

    Long getProductId() {
        return productId;
    }

    Integer getQuantity() {
        return quantity;
    }

    Position getPosition() {
        return position;
    }

    void setQuantity(int q) {
        if (q < 0) throw new IllegalArgumentException("Quantity must be non-negative");
        this.quantity = q;
    }

    void setPosition(Position newPosition) {
        this.position = Objects.requireNonNull(newPosition);
    }
}
