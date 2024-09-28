package rj.com.store.datatransferobjects;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CartItemDTO {
    private int cardItemId;
    private ProductDTO product;
    private int quantity;
    private double totalPrice;
    @JsonIgnore
    private CartDTO cart;
}
