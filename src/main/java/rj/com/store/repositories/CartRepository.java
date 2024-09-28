package rj.com.store.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rj.com.store.enities.Cart;
import rj.com.store.enities.User;

import java.util.Optional;
@Repository
public interface CartRepository extends JpaRepository<Cart,String> {
    Optional<Cart> findByUser(User user);
}
