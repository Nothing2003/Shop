package rj.com.store.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import rj.com.store.enities.Category;
import rj.com.store.enities.Product;

import java.util.Date;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product,String> {
    Optional<Page<Product>> findByTitleContaining (String title, Pageable pageable);
    Optional<Page<Product>> findByLive (boolean isLive, Pageable pageable);
    Optional<Page<Product>> findByAddedDate (Date date, Pageable pageable);
    Optional<Page<Product>> findByPrice ( double price, Pageable pageable);
    Optional<Page<Product>> findByPriceBetween (double minPrice,double maxPrice, Pageable pageable);
    Optional<Page<Product>> findByCategories(Category category, Pageable pageable);
}
