package my.group.productscounter;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("select distinct p.name from Product prod join prod.properties p")
    List<String> findAllUniquePropertyNames();
}
