package my.group.productscounter.project;


import jakarta.persistence.*;
import my.group.productscounter.BaseEntity;

import java.util.ArrayList;
import java.util.List;


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
}
