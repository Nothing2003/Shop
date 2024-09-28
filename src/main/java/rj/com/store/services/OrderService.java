package rj.com.store.services;

import rj.com.store.datatransferobjects.OrderDTO;
import rj.com.store.datatransferobjects.PageableResponse;

import java.util.List;

public interface OrderService {
    //create order
    OrderDTO createOrder(OrderDTO orderDTO,String userId,String cartId);
    //remove order
    void removeOrder(String orderId);
    //get order by user
    List<OrderDTO> getAllOrderOfUser(String userId);
    //get all order
    PageableResponse<OrderDTO> getAllOrder(int pageNumber, int pageSize, String sortBy, String sortDir);
    OrderDTO updateOrder(OrderDTO orderDTO,String orderId);

}
