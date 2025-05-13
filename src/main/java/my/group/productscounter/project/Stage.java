package my.group.productscounter.project;


import jakarta.persistence.*;
import my.group.productscounter.BaseEntity;

import java.util.*;
import java.util.stream.Collectors;


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


    void replaceProducts(List<StageProductSpec> specs) {
        Map<Position, StageProduct> map = products
                .stream()
                .collect(Collectors.toMap(
                        StageProduct::getPosition,
                        stageProduct -> stageProduct));

        List<StageProduct> ordered = new ArrayList<>(specs.size());
        for (StageProductSpec spec : specs){
            Position position = new Position(spec.position());
            StageProduct product = map.remove(position);
            if(product != null) {
                product.setQuantity(spec.quantity());
            } else {
                product = new StageProduct(this, spec.productId(), spec.quantity(), position);
            }
            ordered.add(product);
        }
        map.values().forEach(products::remove);
        products.clear();
        products.addAll(ordered);
    }
}
