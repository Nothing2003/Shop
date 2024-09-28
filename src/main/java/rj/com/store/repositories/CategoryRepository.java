package rj.com.store.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rj.com.store.enities.Category;

import java.util.Optional;
@Repository
public interface CategoryRepository extends JpaRepository<Category,String> {
    Optional<Page<Category>> findByTitleContaining (String keyword, Pageable pageable);
}
