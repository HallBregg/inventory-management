package my.group.productscounter.store;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.util.Objects;


@Embeddable
class Property {
    @Column(name = "property_name", nullable = false)
    private String name;

    @Column(name = "property_value")
    private String value;

    protected Property() {
    }

    Property(String name, String value) {
        this.name = name;
        this.value = value;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;  // reference equality
        if (!(obj instanceof Property that)) return false; // downcast
        return Objects.equals(name, that.name) && Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, value);
    }

    String getName() {
        return name;
    }

    String getValue() {
        return value;
    }
}
