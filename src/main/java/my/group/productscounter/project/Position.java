package my.group.productscounter.project;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.util.Objects;


@Embeddable
class Position{

    @Column(name = "position", nullable = false)
    private int value;

    protected Position() {
    }

    // JPA requirement

    Position(int value) {
        if (value < 0) {
            throw new IllegalArgumentException("Position value cannot be negative.");
        }
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Position position)) return false;
        return value == position.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    public int intValue(){
        return this.value;
    }
}
