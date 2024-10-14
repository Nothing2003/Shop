package rj.com.store.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import rj.com.store.datatransferobjects.OrderDTO;
import rj.com.store.enities.*;
import rj.com.store.exceptions.BadApiRequest;
import rj.com.store.exceptions.ResourceNotFoundException;
import rj.com.store.repositories.CartRepository;
import rj.com.store.repositories.OrderItemRepository;
import rj.com.store.repositories.OrderRepository;
import rj.com.store.repositories.UserRepositories;
import rj.com.store.services.servicesimp.OrderServiceImp;

import java.util.*;

class OrderServiceImpTest {

    @Mock
    private UserRepositories userRepositories;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderItemRepository orderItemRepository;

    @Mock
    private ModelMapper mapper;

    @Mock
    private CartRepository cartRepository;

    @InjectMocks
    private OrderServiceImp orderService;

    private User user;
    private Cart cart;
    private OrderDTO orderDTO;
    private Order order;
    private CartItem cartItem;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        user = new User();
        user.setUserId("user123");
        cart = new Cart();
        cart.setCartId("cart123");
        orderDTO = new OrderDTO();
        orderDTO.setOrderId("order123");
        orderDTO.setBillingName("Test Name");
        orderDTO.setBillingPhone("1234567890");
        orderDTO.setBillingAddress("Test Address");

        order = new Order();
        order.setOrderId("order123");
        order.setUser(user);
        cartItem = new CartItem();
        Product product = new Product();
        product.setPrice(100.0);
        product.setDiscountedPrice(10.0);
        cartItem.setProduct(product);
        cartItem.setQuantity(2);
        cart.setItems(Collections.singletonList(cartItem));
    }

    @Test
    void createOrderSuccess() {
        when(userRepositories.findById("user123")).thenReturn(Optional.of(user));
        when(cartRepository.findById("cart123")).thenReturn(Optional.of(cart));

        when(orderRepository.save(any(Order.class))).thenReturn(order);
        when(mapper.map(any(Order.class), eq(OrderDTO.class))).thenReturn(orderDTO);

        OrderDTO createdOrder = orderService.createOrder(orderDTO, "user123", "cart123");

        assertNotNull(createdOrder);
        verify(orderRepository, times(1)).save(any(Order.class));
    }

    @Test
    void createOrderThrowsResourceNotFoundExceptionForUser() {
        when(userRepositories.findById("user123")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> orderService.createOrder(orderDTO, "user123", "cart123"));

        verify(orderRepository, never()).save(any(Order.class));
    }

    @Test
    void createOrderThrowsResourceNotFoundExceptionForCart() {
        when(userRepositories.findById("user123")).thenReturn(Optional.of(user));
        when(cartRepository.findById("cart123")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> orderService.createOrder(orderDTO, "user123", "cart123"));

        verify(orderRepository, never()).save(any(Order.class));
    }

    @Test
    void createOrderThrowsBadApiRequestForEmptyCart() {
        cart.setItems(Collections.emptyList());
        when(userRepositories.findById("user123")).thenReturn(Optional.of(user));
        when(cartRepository.findById("cart123")).thenReturn(Optional.of(cart));

        assertThrows(BadApiRequest.class, () -> orderService.createOrder(orderDTO, "user123", "cart123"));

        verify(orderRepository, never()).save(any(Order.class));
    }

    @Test
    void removeOrderSuccess() {
        when(orderRepository.findById("order123")).thenReturn(Optional.of(order));

        orderService.removeOrder("order123");

        verify(orderRepository, times(1)).delete(order);
    }

    @Test
    void removeOrderThrowsResourceNotFoundException() {
        when(orderRepository.findById("order123")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> orderService.removeOrder("order123"));
    }

    @Test
    void getAllOrderOfUserSuccess() {
        when(userRepositories.findById("user123")).thenReturn(Optional.of(user));
        when(orderRepository.findByUser(user)).thenReturn(Optional.of(Collections.singletonList(order)));
        when(mapper.map(any(Order.class), eq(OrderDTO.class))).thenReturn(orderDTO);

        List<OrderDTO> orders = orderService.getAllOrderOfUser("user123");

        assertNotNull(orders);
        assertEquals(1, orders.size());
    }

    @Test
    void getAllOrderOfUserThrowsResourceNotFoundExceptionForUser() {
        when(userRepositories.findById("user123")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> orderService.getAllOrderOfUser("user123"));
    }

    @Test
    void updateOrderSuccess() {
        when(orderRepository.findById("order123")).thenReturn(Optional.of(order));
        when(orderRepository.save(any(Order.class))).thenReturn(order);
        when(mapper.map(any(Order.class), eq(OrderDTO.class))).thenReturn(orderDTO);

        OrderDTO updatedOrder = orderService.updateOrder(orderDTO, "order123");

        assertNotNull(updatedOrder);
        verify(orderRepository, times(1)).save(any(Order.class));
    }

    @Test
    void updateOrderThrowsResourceNotFoundException() {
        when(orderRepository.findById("order123")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> orderService.updateOrder(orderDTO, "order123"));
    }
}
