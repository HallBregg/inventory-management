package my.group.productscounter.store;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import java.util.HashSet;
import java.util.Set;

@Entity
class Product {
    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    @NotBlank
    private String name;

    @ElementCollection
    private final Set<Property> properties = new HashSet<>();

    protected Product() {}  // JPA requirement

    Product(String name){
        setName(name);
    }

    void addProperty(String name, String value) {
        this.properties.add(new Property(name, value));
    }

    void setName(String name) {
        if (name == null || name.isBlank()) throw new IllegalArgumentException("Product name must not be blank.");
        this.name = name;
    }

    String getName() {
        return this.name;
    }

    Long getId() {
        return id;
    }

    Set<Property> getProperties() {
        return this.properties;
    }
}
