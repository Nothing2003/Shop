package rj.com.store.services;

import rj.com.store.datatransferobjects.OrderDTO;
import rj.com.store.datatransferobjects.PageableResponse;

import java.util.List;

/**
 * OrderService interface defines methods for managing orders in the system.
 * It includes operations for creating, removing, updating, and retrieving orders.
 */
public interface OrderService {

    /**
     * Creates a new order for a given user and cart.
     *
     * @param orderDTO The data transfer object containing order details.
     * @param userId   The ID of the user placing the order.
     * @param cartId   The ID of the cart from which the order is created.
     * @return An OrderDTO object containing the details of the created order.
     */
    OrderDTO createOrder(OrderDTO orderDTO, String userId, String cartId);

    /**
     * Removes an order from the system based on the order ID.
     *
     * @param orderId The ID of the order to be removed.
     */
    void removeOrder(String orderId);

    /**
     * Retrieves all orders placed by a specific user.
     *
     * @param userId The ID of the user whose orders are to be retrieved.
     * @return A list of OrderDTO objects representing the user's orders.
     */
    List<OrderDTO> getAllOrderOfUser(String userId);

    /**
     * Retrieves all orders in a paginated and sorted manner.
     *
     * @param pageNumber The page number to retrieve (0-based).
     * @param pageSize   The number of items per page.
     * @param sortBy     The field by which to sort the orders.
     * @param sortDir    The direction to sort ("asc" or "desc").
     * @return A PageableResponse object containing the paginated list of OrderDTOs.
     */
    PageableResponse<OrderDTO> getAllOrder(int pageNumber, int pageSize, String sortBy, String sortDir);

    /**
     * Updates an existing order based on the provided order data and order ID.
     *
     * @param orderDTO The data transfer object containing updated order details.
     * @param orderId  The ID of the order to be updated.
     * @return An OrderDTO object representing the updated order.
     */
    OrderDTO updateOrder(OrderDTO orderDTO, String orderId);

}
