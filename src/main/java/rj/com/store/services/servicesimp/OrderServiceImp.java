package rj.com.store.services.servicesimp;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import rj.com.store.datatransferobjects.OrderDTO;
import rj.com.store.datatransferobjects.PageableResponse;
import rj.com.store.enities.*;
import rj.com.store.exceptions.BadApiRequest;
import rj.com.store.exceptions.ResourceNotFoundException;
import rj.com.store.helper.Helper;
import rj.com.store.repositories.CartRepository;
import rj.com.store.repositories.OrderItemRepository;
import rj.com.store.repositories.OrderRepository;
import rj.com.store.repositories.UserRepositories;
import rj.com.store.services.OrderService;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Service
public class OrderServiceImp implements OrderService {
    @Autowired
    private UserRepositories userRepositories;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OrderItemRepository orderItemRepository;
    @Autowired
    private ModelMapper mapper;
    @Autowired
    private CartRepository cartRepository;
    @Override
    public OrderDTO createOrder(OrderDTO orderDTO, String userId,String cartId) {
        //fetch user
        User user=userRepositories.findById(userId).orElseThrow(()->new ResourceNotFoundException("User not found given id"));
        //fetch cart
        Cart cart=cartRepository.findById(cartId).orElseThrow(()->new ResourceNotFoundException("User not found given id"));
        List<CartItem> cartItems=cart.getItems();
        if (cartItems.isEmpty()){
            throw new BadApiRequest("Invalid number of item in cart");
        }
        orderDTO.setOrderId(UUID.randomUUID().toString());
        Order order= Order.builder()
                .billingName(orderDTO.getBillingName())
                .billingPhone(orderDTO.getBillingPhone())
                .orderId(orderDTO.getOrderId())
                .billingAddress(orderDTO.getBillingAddress())
                .orderDate(new Date())
                .deliveredDate(null)
                .paymentStatus(orderDTO.getPaymentStatus())
                .orderStatus(orderDTO.getOrderStatus())
                .user(user)
                .build();
        AtomicReference<Double> orderAmount=new AtomicReference<>(0.0);
        List<OrderItem> orderItems= cartItems.stream().map(cartItem -> {
           OrderItem orderItem= OrderItem.builder()
                    .product(cartItem.getProduct())
                    .quantity(cartItem.getQuantity())
                    .totalPrice(
                            cartItem.getQuantity()*(cartItem.getProduct().getPrice()-cartItem.getProduct().getDiscountedPrice())
                    )
                    .order(order)
                    .build();
           orderAmount.set(orderAmount.get()+orderItem.getTotalPrice());
           return orderItem;
        }).toList();
       order.setOrderItems(orderItems);
       order.setTotalAmount(orderAmount.get());
       cart.getItems().clear();
       cartRepository.save(cart);
       Order savedOrder= orderRepository.save(order);
       return mapper.map(savedOrder,OrderDTO.class);
    }

    @Override
    public void removeOrder(String orderId) {
        Order order= orderRepository.findById(orderId).orElseThrow(()->new ResourceNotFoundException("Order is not found."));
        orderRepository.delete(order);
    }

    @Override
    public List<OrderDTO> getAllOrderOfUser(String userId) {
        User user=userRepositories.findById(userId).orElseThrow(()->new ResourceNotFoundException("User is not found."));
        List<Order> orders=orderRepository.findByUser(user).orElseThrow(()->new ResourceNotFoundException("Orders is not found."));
        return orders.stream().map(order -> mapper.map(order,OrderDTO.class)).collect(Collectors.toList());
    }

    @Override
    public PageableResponse<OrderDTO> getAllOrder(int pageNumber, int pageSize, String sortBy, String sortDir) {
       Page<Order> page=orderRepository.findAll(PageRequest
                .of(pageNumber, pageSize,
                        (sortDir.equalsIgnoreCase("desc"))?
                                (Sort.by(sortBy).descending())
                                :
                                (Sort.by(sortBy).ascending())
                ));
       return Helper.getPageableResponse(page,OrderDTO.class);
    }

    @Override
    public OrderDTO updateOrder(OrderDTO orderDTO, String orderId) {

        Order order=orderRepository.findById(orderId).orElseThrow(()->new ResourceNotFoundException("Order not found."));
        order.setOrderStatus(orderDTO.getOrderStatus());
        order.setPaymentStatus(orderDTO.getPaymentStatus());
        order.setDeliveredDate(orderDTO.getDeliveredDate());
        order.setBillingAddress(orderDTO.getBillingAddress());
        order.setBillingName(orderDTO.getBillingName());
        order.setBillingPhone(orderDTO.getBillingPhone());
        Order saveOrder= orderRepository.save(order);
        return mapper.map(saveOrder,OrderDTO.class);
    }
}