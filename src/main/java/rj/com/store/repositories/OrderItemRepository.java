package rj.com.store.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import rj.com.store.enities.OrderItem;

public interface OrderItemRepository extends JpaRepository<OrderItem,Integer> {

}
