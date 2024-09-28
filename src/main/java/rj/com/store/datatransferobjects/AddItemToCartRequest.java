package rj.com.store.datatransferobjects;

import lombok.*;

@AllArgsConstructor@NoArgsConstructor@Getter@Setter@Builder
public class AddItemToCartRequest {
    public String  productId;
    public int quantity;
}
