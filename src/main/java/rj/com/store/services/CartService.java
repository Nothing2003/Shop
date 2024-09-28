package rj.com.store.services;

import rj.com.store.datatransferobjects.AddItemToCartRequest;
import rj.com.store.datatransferobjects.CartDTO;

public interface CartService
{
    //add items to cart:
    //case1: cart for user is not available   we will create the cart and then add the item
    //case2: cart available add the items to cart
    CartDTO addItemToCart(String userId, AddItemToCartRequest request);
    //remove item from cart
    void removeItemFromCart(String userId,int cartItem);
    void clearCart(String userId);
    CartDTO getCartByUser(String userId);

}
