package rj.com.store.enities;

import lombok.*;

import jakarta.persistence.*;

@AllArgsConstructor@NoArgsConstructor@Getter@Setter@Builder
@Entity
@Table(name = "order_item")
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int orderItem;
    private int quantity;
    private double totalPrice;
    @OneToOne
    @JoinColumn(name = "product_id")
    private Product product;
    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;
}
