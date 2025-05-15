package my.group.productscounter;
//
//
//import static org.assertj.core.api.Assertions.*;
//
//import my.group.productscounter.store.Product;
//import my.group.productscounter.store.ProductRepository;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
//
//import java.util.List;
//
//
//@DataJpaTest
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
//class ProductRepositoryTest {
//    private TestEntityManager entityManager;
//    private ProductRepository productRepository;
//
//    @Autowired
//    ProductRepositoryTest(TestEntityManager entityManager, ProductRepository productRepository) {
//        this.entityManager = entityManager;
//        this.productRepository = productRepository;
//    }
//
//    @Test
//    void findAllUniquePropertyNames_returnsUniquePropertyNames() {
//        // given
//        Product product1 = new Product();
//        product1.setName("product1");
//        product1.addProperty("property1", "value1");
//        product1.addProperty("property2", "value1");
//        entityManager.persist(product1);
//
//        Product product2 = new Product();
//        product2.setName("product2");
//        product2.addProperty("property1", "value1");
//        product2.addProperty("property3", "value1");
//        entityManager.persist(product2);
//
//        // when
//        List<String> propertyNames = productRepository.findAllUniquePropertyNames();
//
//        // then
//        assertThat(propertyNames).containsExactlyInAnyOrder(
//"property1", "property2", "property3"
//        );
//    }
//
//    @Test
//    void findAll_returnsAllProducts() {
//        // given
//        Product product1 = new Product();
//        product1.setName("product1");
//        product1.addProperty("property1", "value1");
//        product1.addProperty("property2", "value1");
//        entityManager.persist(product1);
//
//        Product product2 = new Product();
//        product2.setName("product2");
//        product2.addProperty("property1", "value1");
//        product2.addProperty("property3", "value1");
//        entityManager.persist(product2);
//
//        // when
//        List<Product> products = productRepository.findAll();
//
//        // then
//        assertThat(products).containsExactlyInAnyOrder(product1, product2);
//    }
//}