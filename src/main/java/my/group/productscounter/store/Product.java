package my.group.productscounter.store;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
class Product {
    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String name;

    @ElementCollection
    private Set<Property> properties = new HashSet<>();

    protected Product() {}

    void addProperty(Property property) {
        this.properties.add(property);
    }

    void addProperty(String name, String value) {
        this.properties.add(new Property(name, value));
    }

    void setName(String name) { this.name = name; }

    public String getName() { return this.name; }

    public Long getId() { return id; }

    public Set<Property> getProperties() { return this.properties; }
}
