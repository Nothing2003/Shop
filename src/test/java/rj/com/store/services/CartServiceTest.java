package rj.com.store.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
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
import rj.com.store.services.servicesimp.CartServiceImp;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CartServiceImpTest {

    @InjectMocks
    private CartServiceImp cartService;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private UserRepositories userRepositories;

    @Mock
    private CartRepository cartRepository;

    @Mock
    private ModelMapper mapper;

    @Mock
    private CartItemRepository cartItemRepository;

    private User user;
    private Cart cart;
    private Product product;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        user = new User();
        user.setUserId(UUID.randomUUID().toString());

        product = new Product();
        product.setProductId(UUID.randomUUID().toString());
        product.setPrice(100.0);
        product.setDiscountedPrice(10.0);

        cart = new Cart();
        cart.setUser(user);
    }

    @Test
    void addItemToCartSuccessNewItem() {
        AddItemToCartRequest request = new AddItemToCartRequest();
        request.setProductId(product.getProductId());
        request.setQuantity(2);

        when(productRepository.findById(anyString())).thenReturn(Optional.of(product));
        when(userRepositories.findById(anyString())).thenReturn(Optional.of(user));
        when(cartRepository.findByUser(any(User.class))).thenReturn(Optional.of(cart));
        when(cartRepository.save(any(Cart.class))).thenReturn(cart);
        when(mapper.map(any(Cart.class), eq(CartDTO.class))).thenReturn(new CartDTO());
        CartDTO result = cartService.addItemToCart(user.getUserId(), request);
        assertNotNull(result);
        verify(cartRepository, times(1)).save(any(Cart.class));
        verify(mapper, times(1)).map(any(Cart.class), eq(CartDTO.class));
    }

    @Test
    void addItemToCartFailureProductNotFound() {

        AddItemToCartRequest request = new AddItemToCartRequest();
        request.setProductId(product.getProductId());
        request.setQuantity(2);

        when(productRepository.findById(anyString())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () ->
                cartService.addItemToCart(user.getUserId(), request));
    }

    @Test
    void addItemToCartFailureInvalidQuantity() {

        AddItemToCartRequest request = new AddItemToCartRequest();
        request.setProductId(product.getProductId());
        request.setQuantity(-1);
        assertThrows(BadApiRequest.class, () ->
                cartService.addItemToCart(user.getUserId(), request));
    }
}
