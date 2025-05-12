package my.group.productscounter.project;


import jakarta.persistence.*;
import my.group.productscounter.BaseEntity;

import java.util.ArrayList;
import java.util.List;


@Entity
class Stage extends BaseEntity {

    @ManyToOne(optional=false)
    private Project project;

    @Column(nullable=false)
    private String name;

    @OneToMany(
        mappedBy="stage",
        cascade=CascadeType.ALL,
        orphanRemoval=true
    )

    private List<StageProduct> products = new ArrayList<>();

    protected Stage() {}  // JPA requirement
}
