package rj.com.store.datatransferobjects;

import lombok.*;
import rj.com.store.enities.Cart;
import rj.com.store.enities.CartItem;
import rj.com.store.enities.Product;
import rj.com.store.enities.User;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CartDTO {
    private String cartId;
    private Date createAt;
    private UserDTO user;
    private List<CartItemDTO> items=new ArrayList<>();
}
