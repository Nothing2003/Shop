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
    private final UserRepositories userRepositories;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final ModelMapper mapper;
    private final CartRepository cartRepository;
    /**
     * Constructor for the OrderServiceImp class.
     *
     * @param userRepositories the repository for accessing user data
     * @param orderRepository the repository for managing orders
     * @param orderItemRepository the repository for managing order items
     * @param mapper the ModelMapper instance for converting between entity and DTO
     * @param cartRepository the repository for managing shopping carts
     */
    public OrderServiceImp(UserRepositories userRepositories, OrderRepository orderRepository,
                           OrderItemRepository orderItemRepository, ModelMapper mapper,
                           CartRepository cartRepository) {
        this.userRepositories = userRepositories;
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.mapper = mapper;
        this.cartRepository = cartRepository;
    }
    /**
     * Creates a new order based on the provided order details, user ID, and cart ID.
     *
     * @param orderDTO the details of the order to be created
     * @param userId the ID of the user placing the order
     * @param cartId the ID of the cart associated with the order
     * @return the created OrderDTO object containing the order details
     * @throws ResourceNotFoundException if the user or cart is not found
     * @throws BadApiRequest if there are no items in the cart
     */
    @Override
    public OrderDTO createOrder(OrderDTO orderDTO, String userId,String cartId) {
        // Fetch the user by userId, throwing an exception if not found
        User user = userRepositories.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found given id"));

        // Fetch the cart by cartId, throwing an exception if not found
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found given id"));

        // Retrieve the list of items in the cart
        List<CartItem> cartItems = cart.getItems();

        // Check if the cart is empty and throw an exception if true
        if (cartItems.isEmpty()) {
            throw new BadApiRequest("Invalid number of items in cart");
        }

        // Generate a unique order ID for the new order
        orderDTO.setOrderId(UUID.randomUUID().toString());

        // Build the Order object from the provided OrderDTO and additional information
        Order order = Order.builder()
                .billingName(orderDTO.getBillingName())
                .billingPhone(orderDTO.getBillingPhone())
                .orderId(orderDTO.getOrderId())
                .billingAddress(orderDTO.getBillingAddress())
                .orderDate(new Date())
                .deliveredDate(null) // Initially, the delivered date is set to null
                .paymentStatus(orderDTO.getPaymentStatus())
                .orderStatus(orderDTO.getOrderStatus())
                .user(user) // Set the user associated with this order
                .build();

        // Use an AtomicReference to keep track of the total order amount
        AtomicReference<Double> orderAmount = new AtomicReference<>(0.0);

        // Map each CartItem to an OrderItem while calculating the total order amount
        List<OrderItem> orderItems = cartItems.stream().map(cartItem -> {
            // Create an OrderItem based on the cart item
            OrderItem orderItem = OrderItem.builder()
                    .product(cartItem.getProduct())
                    .quantity(cartItem.getQuantity())
                    .totalPrice(cartItem.getQuantity() *
                            (cartItem.getProduct().getPrice() - cartItem.getProduct().getDiscountedPrice()))
                    .order(order) // Associate the OrderItem with the created order
                    .build();

            // Update the total order amount
            orderAmount.set(orderAmount.get() + orderItem.getTotalPrice());
            return orderItem; // Return the created OrderItem
        }).toList();

        // Set the list of OrderItems and the total amount for the order
        order.setOrderItems(orderItems);
        order.setTotalAmount(orderAmount.get());
        cartRepository.save(cart); // Save the updated cart

        // Save the created order in the repository and return the mapped OrderDTO
        Order savedOrder = orderRepository.save(order);
        return mapper.map(savedOrder, OrderDTO.class);
    }
    /**
     * Removes an order from the repository based on the provided order ID.
     *
     * @param orderId the ID of the order to be removed
     * @throws ResourceNotFoundException if the order with the specified ID is not found
     */
    @Override
    public void removeOrder(String orderId) {
        // Fetch the order by its ID, throwing an exception if not found
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order is not found."));

        // Delete the fetched order from the repository
        orderRepository.delete(order);
    }
    /**
     * Retrieves a list of orders for a specific user.
     *
     * @param userId the ID of the user whose orders are to be retrieved
     * @return a list of OrderDTOs associated with the specified user
     * @throws ResourceNotFoundException if the user is not found, or if no orders exist for the user
     */
    @Override
    public List<OrderDTO> getAllOrderOfUser(String userId) {
        // Fetch the user by their ID, throwing an exception if not found
        User user = userRepositories.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User is not found."));

        // Retrieve the list of orders associated with the user, throwing an exception if not found
        List<Order> orders = orderRepository.findByUser(user)
                .orElseThrow(() -> new ResourceNotFoundException("Orders is not found."));

        // Map the list of Order entities to a list of OrderDTOs and return it
        return orders.stream()
                .map(order -> mapper.map(order, OrderDTO.class))
                .collect(Collectors.toList());
    }

    /**
     * Retrieves a paginated list of orders with sorting options.
     *
     * @param pageNumber the number of the page to retrieve (0-indexed)
     * @param pageSize   the number of orders per page
     * @param sortBy     the field by which to sort the orders
     * @param sortDir    the direction of sorting, either "asc" for ascending or "desc" for descending
     * @return a PageableResponse containing the list of OrderDTOs for the specified page
     */
    @Override
    public PageableResponse<OrderDTO> getAllOrder(int pageNumber, int pageSize, String sortBy, String sortDir) {
        // Create a PageRequest object for pagination and sorting
        Page<Order> page = orderRepository.findAll(PageRequest
                .of(pageNumber, pageSize,
                        // Determine a sorting direction based on the sortDir parameter
                        sortDir.equalsIgnoreCase("desc") ?
                                Sort.by(sortBy).descending() :
                                Sort.by(sortBy).ascending()
                ));

        // Convert the Page<Order> to a PageableResponse<OrderDTO> using the Helper class
        return Helper.getPageableResponse(page, OrderDTO.class);
    }

    /**
     * Updates an existing order based on the provided order details.
     *
     * @param orderDTO the updated order details to apply
     * @param orderId  the ID of the order to be updated
     * @return the updated OrderDTO representing the modified order
     */
    @Override
    public OrderDTO updateOrder(OrderDTO orderDTO, String orderId) {

        // Fetch the existing order from the repository using the provided order ID
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found."));
        // Update the order's properties with values from the orderDTO
        order.setOrderStatus(orderDTO.getOrderStatus());         // Update order status
        order.setPaymentStatus(orderDTO.getPaymentStatus());     // Update payment status
        order.setDeliveredDate(orderDTO.getDeliveredDate());     // Update delivered date
        order.setBillingAddress(orderDTO.getBillingAddress());     // Update billing address
        order.setBillingName(orderDTO.getBillingName());         // Update billing name
        order.setBillingPhone(orderDTO.getBillingPhone());       // Update billing phone number
        // Save the updated order back to the repository
        Order savedOrder = orderRepository.save(order);

        // Convert the saved order back to an OrderDTO and return it
        return mapper.map(savedOrder, OrderDTO.class);
    }
}