package my.group.productscounter.project;


import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;


@Entity
class Stage {
    @Id
    @GeneratedValue
    private Long id;

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
