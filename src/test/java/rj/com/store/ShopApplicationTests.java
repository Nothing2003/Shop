package rj.com.store;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

@SpringBootTest
@ContextConfiguration(classes = ShopApplication.class)
class ShopApplicationTests {
    @Test
    void contextLoads() {
        System.out.println("Testing our project");
    }
}
