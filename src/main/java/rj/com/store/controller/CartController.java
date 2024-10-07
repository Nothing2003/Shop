package rj.com.store.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import rj.com.store.datatransferobjects.AddItemToCartRequest;
import rj.com.store.datatransferobjects.ApiResponseMessage;
import rj.com.store.datatransferobjects.CartDTO;
import rj.com.store.datatransferobjects.CartItemDTO;
import rj.com.store.services.CartService;

@RestController
@RequestMapping("/carts/v1")
@SecurityRequirement(name = "scheme")
@Tag(name = "Cart Controller ",description = "This is cart Api for cart operation")
public class CartController {
    CartService cartService;
    public CartController(CartService cartService){
        this.cartService=cartService;
    }
    //add item to cart
    @PreAuthorize("hasAnyRole('NORMAL','ADMIN')")
    @PostMapping("/{userId}")
    @Operation(summary = "Create cart by user Id ")
    public ResponseEntity<CartDTO> addItemToCart(@PathVariable("userId") String userId ,@RequestBody AddItemToCartRequest request ){
        return new ResponseEntity<>(cartService.addItemToCart(userId,request), HttpStatus.ACCEPTED);
    }
    @PreAuthorize("hasAnyRole('NORMAL','ADMIN')")
    @DeleteMapping("/{userId}/items/{itemId}")
    @Operation(summary = "Removed item from cart by user and item Id")
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
    @PreAuthorize("hasAnyRole('NORMAL','ADMIN')")
    @DeleteMapping("/{userId}")
    @Operation(summary = "Clear Cart")
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
    @PreAuthorize("hasAnyRole('NORMAL','ADMIN')")
    @GetMapping("/{userId}")
    @Operation(summary = "Get cart by user Id")
    public ResponseEntity<CartDTO> getCart(@PathVariable("userId") String userId ){
        return new ResponseEntity<>(cartService.getCartByUser(userId), HttpStatus.ACCEPTED);
    }
}
