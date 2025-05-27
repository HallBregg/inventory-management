package my.group.productscounter.store;

import my.group.productscounter.store.dto.CreateProductDto;
import my.group.productscounter.store.exception.ProductCouldNotBeCreatedException;
import my.group.productscounter.store.exception.ProductNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class ProductStoreServiceTest {

    @Mock
    private ProductRepository repo;

    @InjectMocks
    private ProductStoreService productStoreService;

    @Test
    void createProductWithEmptyName(){
        ProductCouldNotBeCreatedException exc = assertThrows(
                ProductCouldNotBeCreatedException.class,
                () -> productStoreService.create(new CreateProductDto("", new HashSet<>()))
        );

        assertEquals("PRODUCT_CREATE_ERROR", exc.getCode());
        assertEquals("Could not create the product.", exc.getMessage());
    }

    @Test
    void findById_productNotFound(){
        when(repo.findById(1L)).thenThrow(new ProductNotFoundException());

        ProductNotFoundException exc = assertThrows(
                ProductNotFoundException.class,
                () -> productStoreService.findById(1L)
        );
        assertEquals("PRODUCT_NOT_FOUND", exc.getCode());
        assertNull(exc.getMessage());
    }
}
