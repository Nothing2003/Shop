package rj.com.store.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import rj.com.store.datatransferobjects.ApiResponseMessage;
import rj.com.store.datatransferobjects.OrderDTO;
import rj.com.store.datatransferobjects.PageableResponse;
import rj.com.store.helper.AppCon;
import rj.com.store.services.OrderService;

import java.util.List;
/**
 * Controller for handling order-related APIs.
 *
 * This class provides endpoints for creating, updating, deleting, and fetching orders.
 * The operations are secured based on roles.
 */
@RestController
@RequestMapping("/orders/v1")
@SecurityRequirement(name = "scheme")
@Tag(name = "Order Controller", description = "This API handles order operations such as creating, updating, fetching, and deleting orders.")
public class OrderController {
    OrderService orderService;
    /**
     * Constructor to initialize the OrderService.
     *
     * @param orderService the service handling business logic for orders
     */
    public OrderController(OrderService orderService){
        this.orderService=orderService;
    }
    /**
     * Creates an order for a user from their cart.
     *
     * This operation is accessible to users with ADMIN or NORMAL roles.
     *
     * @param orderDTO the details of the order to be created
     * @param userId the ID of the user placing the order
     * @param cartId the ID of the cart being ordered
     * @return a ResponseEntity containing the created OrderDTO
     */
    @PostMapping("/create/user/{userId}/cart/{cartId}")
    @Operation(summary = "Create Order")
    public ResponseEntity<OrderDTO> createOrder(
            @RequestBody OrderDTO orderDTO,
            @PathVariable("userId") String userId,
            @PathVariable("cartId") String cartId){
        return new ResponseEntity<>(orderService.createOrder(orderDTO,userId,cartId), HttpStatus.CREATED);
    }
    /**
     * Deletes an order by its ID.
     *
     * This operation is restricted to ADMIN role.
     *
     * @param orderId the ID of the order to be deleted
     * @return a ResponseEntity with a success message
     */
    @DeleteMapping("/{orderId}")
    @Operation(summary = "Delete order by id")
    public ResponseEntity<ApiResponseMessage> deleteOrder(@PathVariable("orderId") String orderId){
        orderService.removeOrder(orderId);
        return new ResponseEntity<>(ApiResponseMessage.builder()
                .httpStatus(HttpStatus.OK)
                .success(true)
                .massage("Order is successfully removed !!")
                .build(),HttpStatus.OK);
    }

    /**
     * Fetches all orders for a specific user.
     *
     * This operation is accessible to users with ADMIN or NORMAL roles.
     *
     * @param userId the ID of the user whose orders are being fetched
     * @return a ResponseEntity containing the list of OrderDTOs
     */
    @PreAuthorize("hasAnyRole('"+AppCon.ROLE_NORMAL+"','"+AppCon.ROLE_ADMIN+"')")
    @GetMapping("{userId}")
    @Operation(summary = "Get all order by user Id")
    public ResponseEntity<List<OrderDTO>> getAllOrderByUserId(@PathVariable("userId") String userId ){
        return new ResponseEntity<>(orderService.getAllOrderOfUser(userId),HttpStatus.OK);
    }
    /**
     * Fetches all orders with pagination.
     *
     * This operation is restricted to ADMIN role.
     *
     * @param pageNumber the page number for pagination
     * @param pageSize the number of items per page
     * @param sortBy the attribute to sort by
     * @param sortDir the sort direction (ASC/DESC)
     * @return a ResponseEntity containing the paginated response of OrderDTOs
     */
    @PreAuthorize("hasRole('"+AppCon.ROLE_ADMIN+"')")
    @GetMapping
    @Operation(summary = "Get all order")
    public ResponseEntity<PageableResponse<OrderDTO>> getAllOrder(
            @RequestParam(value = "pageNumber", defaultValue = AppCon.Page_Number, required = false) int pageNumber,
            @RequestParam(value = "pageSize", defaultValue = AppCon.Page_Size, required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = "orderId", required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppCon.Sort_Dir, required = false) String sortDir
    )
    {
        return new ResponseEntity<>(orderService.getAllOrder(pageNumber,pageSize,sortBy,sortDir),HttpStatus.OK);
    }
    /**
     * Updates an existing order by its ID.
     *
     * This operation is accessible to users with ADMIN or NORMAL roles.
     *
     * @param orderDTO the updated order details
     * @param orderId the ID of the order to be updated
     * @return a ResponseEntity containing the updated OrderDTO
     */
    @PutMapping("/update/{orderId}")
    @Operation(summary = "Update order by order Id")
    public ResponseEntity<OrderDTO> updateOrder(
            @RequestBody OrderDTO orderDTO,
            @PathVariable("orderId") String orderId)
    {
        return new ResponseEntity<>(orderService.updateOrder(orderDTO,orderId), HttpStatus.OK);
    }

}
