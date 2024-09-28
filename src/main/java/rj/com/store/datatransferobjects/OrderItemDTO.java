package rj.com.store.datatransferobjects;

import lombok.*;
import rj.com.store.enities.Order;
import rj.com.store.enities.Product;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
@AllArgsConstructor@NoArgsConstructor@Getter@Builder@Setter
public class OrderItemDTO
{
    private int orderItem;
    private int quantity;
    private double totalPrice;
    private ProductDTO product;
}
