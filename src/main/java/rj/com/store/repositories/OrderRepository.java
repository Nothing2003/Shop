package rj.com.store.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import rj.com.store.enities.Order;
import rj.com.store.enities.User;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order,String> {
    Optional<List<Order>> findByUser(User user);
}
