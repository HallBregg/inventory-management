package my.group.productscounter.project;


import jakarta.persistence.*;
import my.group.productscounter.BaseEntity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Entity
class Stage extends BaseEntity {

    @ManyToOne(optional = false)
    private Project project;

    @Column(nullable = false)
    private String name;

    @OneToMany(
            mappedBy = "stage",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private final List<StageProduct> products = new ArrayList<>();

    protected Stage() {
    }  // JPA requirement

    Stage(String name, Project project) {
        this.name = name;
        this.project = project;
    }

    String getName() {
        return name;
    }

    List<StageProduct> getProducts() {
        return products;
    }

    void replaceProducts(List<StageProductSpec> products) {
        Set<Position> seen = new HashSet<>();
        products.forEach(product -> {
            Position position = new Position(product.position());
            if (!seen.add(position)) throw new IllegalStateException("Duplicate position: " + position);
        });

        this.products.clear();
        for (StageProductSpec product : products) {
            this.products.add(
                    new StageProduct(
                            this,
                            product.productId(),
                            product.quantity(),
                            new Position(product.position())
                    )
            );
        }
    }
}
