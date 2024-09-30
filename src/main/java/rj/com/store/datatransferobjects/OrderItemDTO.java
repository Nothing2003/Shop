package rj.com.store.datatransferobjects;

import lombok.*;

@AllArgsConstructor@NoArgsConstructor@Getter@Builder@Setter
public class OrderItemDTO
{
    private int orderItem;
    private int quantity;
    private double totalPrice;
    private ProductDTO product;
}
