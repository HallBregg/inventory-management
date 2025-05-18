package my.group.productscounter.project;


import jakarta.persistence.*;
import my.group.productscounter.BaseEntity;
import my.group.productscounter.project.dto.StageProductDto;

import java.util.*;


@Entity
class Stage extends BaseEntity {

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Project project;

    @Column(nullable = false)
    private String name;

    @OneToMany(
            mappedBy = "stage",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @OrderBy("position.value")
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

    public void replaceProducts(List<StageProductDto> specs) {
        // 1) validate unique positions
        Set<Position> seen = new HashSet<>();
        for (StageProductDto spec : specs) {
            Position pos = new Position(spec.position());
            if (!seen.add(pos)) {
                throw new IllegalStateException("Duplicate position: " + pos.getValue());
            }
        }

        // 2) clear old
        products.clear();  // orphanRemoval deletes them

        // 3) add new ones
        for (StageProductDto spec : specs) {
            Position pos = new Position(spec.position());
            products.add(new StageProduct(this,
                    spec.productId(),
                    pos,
                    spec.quantity()));
        }
    }

    void setName(String name) {
        this.name = name;
    }
}
