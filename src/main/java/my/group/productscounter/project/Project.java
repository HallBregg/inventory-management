package my.group.productscounter.project;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name="projects")
class Project{

    @GeneratedValue
    @Id
    private Long id;

    @Column(nullable = false)
    private String name;

    @OneToMany(
        mappedBy="project",
        cascade=CascadeType.ALL,
        orphanRemoval=true
    )
    private List<Stage> stages = new ArrayList<>();

    protected Project(){}  // JPA requirement
}
