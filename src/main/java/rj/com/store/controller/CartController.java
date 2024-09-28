package rj.com.store.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rj.com.store.datatransferobjects.AddItemToCartRequest;
import rj.com.store.datatransferobjects.ApiResponseMessage;
import rj.com.store.datatransferobjects.CartDTO;
import rj.com.store.datatransferobjects.CartItemDTO;
import rj.com.store.services.CartService;

@RestController
@RequestMapping("/carts")
public class CartController {
    CartService cartService;
    public CartController(CartService cartService){
        this.cartService=cartService;
    }
    //add item to cart
    @PostMapping("/{userId}")
    public ResponseEntity<CartDTO> addItemToCart(@PathVariable("userId") String userId ,@RequestBody AddItemToCartRequest request ){
        return new ResponseEntity<>(cartService.addItemToCart(userId,request), HttpStatus.ACCEPTED);
    }
    @DeleteMapping("/{userId}/items/{itemId}")
    public ResponseEntity<ApiResponseMessage> removeItemFromCart(@PathVariable("userId") String userId ,@PathVariable("itemId") int  itemId){
        cartService.removeItemFromCart(userId,itemId);
        return new ResponseEntity<>(
                ApiResponseMessage.builder()
                        .massage("Item is removed !!")
                        .success(true)
                        .httpStatus(HttpStatus.OK)
                        .build(),
                HttpStatus.OK);
    }
    @DeleteMapping("/{userId}")
    public ResponseEntity<ApiResponseMessage> clearCart(@PathVariable("userId") String userId ){
        cartService.clearCart(userId);
        return new ResponseEntity<>(
                ApiResponseMessage.builder()
                        .massage("Cart is clear !!")
                        .success(true)
                        .httpStatus(HttpStatus.OK)
                        .build(),
                HttpStatus.OK);
    }
    @GetMapping("/{userId}")
    public ResponseEntity<CartDTO> getCart(@PathVariable("userId") String userId ){
        return new ResponseEntity<>(cartService.getCartByUser(userId), HttpStatus.ACCEPTED);
    }
}
