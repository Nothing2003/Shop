package rj.com.store.services;

import rj.com.store.datatransferobjects.AddItemToCartRequest;
import rj.com.store.datatransferobjects.CartDTO;

public interface CartService {

    /**
     * Adds an item to the user's cart.
     *
     *
     * @param userId the unique identifier of the user
     * @param request the request object containing details of the item to be added
     * @return the updated cart details as a CartDTO object
     */
    CartDTO addItemToCart(String userId, AddItemToCartRequest request);

    /**
     * Removes an item from the user's cart.
     *
     * @param userId the unique identifier of the user
     * @param cartItem the identifier of the item to be removed from the cart
     */
    void removeItemFromCart(String userId, int cartItem);

    /**
     * Clears all items from the user's cart.
     *
     * @param userId the unique identifier of the user
     */
    void clearCart(String userId);

    /**
     * Retrieves the cart associated with the user.
     *
     * @param userId the unique identifier of the user
     * @return the user's cart as a CartDTO object, or null if no cart exists
     */
    CartDTO getCartByUser(String userId);
}
