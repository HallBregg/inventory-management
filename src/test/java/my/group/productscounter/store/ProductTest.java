package my.group.productscounter.store;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


class ProductTest {

    @Test
    void addPropertyDuplicate(){
        Product product = new Product();
        product.setName("Product1");

        product.addProperty("property1", "value1");
        product.addProperty("property1", "value1");

        assertEquals(1, product.getProperties().size());
    }

    @Test
    void productInstanceWithEmptyName(){
        assertThrows(IllegalArgumentException.class, () -> {new Product("");});
    }
}
