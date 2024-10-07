package rj.com.store.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import rj.com.store.enities.Category;
import rj.com.store.enities.Product;
import rj.com.store.repositories.ProductRepository;

import java.util.UUID;

@SpringBootTest
public class ProductServiceTest {
    @Autowired
    private ProductService productService;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private CategoryService categoryService;
    Product product;
    Category category;
    @BeforeEach
    public void setUp() {
//        product =new  Product();
//        product.setCategory(category);
//        product

    }
}
