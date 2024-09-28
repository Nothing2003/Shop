package rj.com.store.services.servicesimp;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rj.com.store.datatransferobjects.AddItemToCartRequest;
import rj.com.store.datatransferobjects.CartDTO;
import rj.com.store.enities.Cart;
import rj.com.store.enities.CartItem;
import rj.com.store.enities.Product;
import rj.com.store.enities.User;
import rj.com.store.exceptions.BadApiRequest;
import rj.com.store.exceptions.ResourceNotFoundException;
import rj.com.store.repositories.CartItemRepository;
import rj.com.store.repositories.CartRepository;
import rj.com.store.repositories.ProductRepository;
import rj.com.store.repositories.UserRepositories;
import rj.com.store.services.CartService;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;
@Service
public class CartServiceImp implements CartService {
    Logger logger= LoggerFactory.getLogger(CartServiceImp.class);
    @Autowired
   private ProductRepository productRepository;
    @Autowired
   private UserRepositories userRepositories;
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private ModelMapper mapper;
    @Autowired
    CartItemRepository cartItemRepository;
    @Override
    public CartDTO addItemToCart(String userId, AddItemToCartRequest request) {
        int quantity= request.getQuantity();
        String productId= request.getProductId();
        if (quantity<=0){
            throw new BadApiRequest("Requested quantity is not valid");
        }
        //fetch the product
        Product product=productRepository.findById(productId).orElseThrow(()->new ResourceNotFoundException("Product it not found"));
        //fetch the user from db
        User user=  userRepositories.findById(userId).orElseThrow(()->new ResourceNotFoundException("User Not found"));
        Cart cart=null;
        try {
            cart=cartRepository.findByUser(user).get();
        }catch (NoSuchElementException e){
          cart=new Cart();
          cart.setCartId(UUID.randomUUID().toString());
          cart.setCreateAt(new Date());
          cart.setUser(user);
        }
        //perform cart operations
        //cart is already present
        AtomicBoolean updated= new AtomicBoolean(false);
        List<CartItem> items = cart.getItems();
        items=items.stream().map(item->{
            if (item.getProduct().getProductId().equalsIgnoreCase(productId)){
                //item is present
                item.setQuantity(quantity);
                item.setTotalPrice(quantity*(product.getPrice()-product.getDiscountedPrice()));
                updated.set(true);
            }
            return item;
        }).collect(Collectors.toList());
        //create item
        if (!updated.get()){
            CartItem cartItem= CartItem.builder()
                    .quantity(quantity)
                    .totalPrice(quantity*(product.getPrice()-product.getDiscountedPrice()))
                    .cart(cart)
                    .product(product)
                    .build();
            cart.getItems().add(cartItem);
        }else {
            cart.setItems(items);
        }
        Cart updatedCart= cartRepository.save(cart);
        return mapper.map(updatedCart,CartDTO.class);
    }
    @Override
    public void removeItemFromCart(String userId, int cartItem) {
      User user=userRepositories.findById(userId).orElseThrow(()->new ResourceNotFoundException("User not found"));
      Cart cart=cartRepository.findByUser(user).orElseThrow(()->new ResourceNotFoundException("Cart not found"));
      CartItem cartItem1= cartItemRepository.findById(cartItem).orElseThrow(()->new ResourceNotFoundException("Cart Item not found"));
      if (cart.getItems().contains(cartItem1)) {
            cart.getItems().remove(cartItem1);
            cartRepository.save(cart);
            cartItemRepository.delete(cartItem1);
      } else {
            throw new BadApiRequest("This cart item is not present in this user's cart.");
      }

    }
    @Override
    public void clearCart(String userId) {
       User user= userRepositories.findById(userId).orElseThrow(()->new ResourceNotFoundException("User not found"));
       logger.info("User found {}",user);
       Cart cart=cartRepository.findByUser(user).orElseThrow(()->new ResourceNotFoundException("Cart not found from this user"));
       logger.info("cart found {}",cart);
        cart.getItems().clear();
        logger.info("cart is removed");
       cartRepository.save(cart);
    }
    @Override
    public CartDTO getCartByUser(String userId) {
        User user= userRepositories.findById(userId).orElseThrow(()->new ResourceNotFoundException("User not found"));
        Cart cart=cartRepository.findByUser(user).orElseThrow(()->new ResourceNotFoundException("Cart not found from this user"));
        return mapper.map(cart,CartDTO.class);
    }
}
