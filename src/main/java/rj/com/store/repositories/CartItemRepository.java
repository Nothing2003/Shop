package rj.com.store.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rj.com.store.enities.CartItem;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem,Integer> {

}
