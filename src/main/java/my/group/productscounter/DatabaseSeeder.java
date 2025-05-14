package my.group.productscounter;

import my.group.productscounter.project.CreateProjectCommand;
import my.group.productscounter.project.ProjectService;
import my.group.productscounter.store.CreateProductCommand;
import my.group.productscounter.store.Product;
import my.group.productscounter.store.ProductStoreService;
import my.group.productscounter.store.PropertyData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.Set;


@Component
@ConditionalOnProperty(name = "database.seed", havingValue = "true")
public class DatabaseSeeder implements CommandLineRunner {
    private final ProductStoreService productStoreService;
    private final ProjectService projectService;

    @Autowired
    public DatabaseSeeder(ProductStoreService productStoreService, ProjectService projectService){
        this.productStoreService = productStoreService;
        this.projectService = projectService;
    }

    @Override
    public void run(String... args) throws Exception {
        Product product1 = productStoreService.create(new CreateProductCommand(
                "product1",
                Set.of(new PropertyData("key1", "value1"), new PropertyData("key2", "value1"))));
        Product product2 =productStoreService.create(new CreateProductCommand(
                "product2",
                Set.of(new PropertyData("key1", "value1"), new PropertyData("key2", "value1"))));
        Product product3 = productStoreService.create(new CreateProductCommand(
                "product3",
                Set.of(new PropertyData("key1", "value1"), new PropertyData("key2", "value1"))));
        Product product4 = productStoreService.create(new CreateProductCommand(
                "product4",
                Set.of(new PropertyData("key1", "value1"), new PropertyData("key2", "value1"))));


    }
}
