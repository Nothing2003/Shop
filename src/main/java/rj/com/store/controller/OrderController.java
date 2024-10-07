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

@RestController
@RequestMapping("/orders//v1")
@SecurityRequirement(name = "scheme")
@Tag(name = "Order Controller ",description = "This is order Api for order operation")
public class OrderController {
    OrderService orderService;
    public OrderController(OrderService orderService){
        this.orderService=orderService;
    }
    @PreAuthorize("hasAnyRole('"+AppCon.ROLE_ADMIN+"','"+AppCon.ROLE_NORMAL+"')")
    @PostMapping("/create/user/{userId}/cart/{cartId}")
    @Operation(summary = "Create Order")
    public ResponseEntity<OrderDTO> createOrder(
            @RequestBody OrderDTO orderDTO,
            @PathVariable("userId") String userId,
            @PathVariable("cartId") String cartId){
        return new ResponseEntity<>(orderService.createOrder(orderDTO,userId,cartId), HttpStatus.CREATED);
    }
    @PreAuthorize("hasRole('"+AppCon.ROLE_ADMIN+"')")
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
    @PreAuthorize("hasAnyRole('"+AppCon.ROLE_NORMAL+"','"+AppCon.ROLE_ADMIN+"')")
    @GetMapping("{userId}")
    @Operation(summary = "Get all order by user Id")
    public ResponseEntity<List<OrderDTO>> getAllOrderByUserId(@PathVariable("userId") String userId ){
        return new ResponseEntity<>(orderService.getAllOrderOfUser(userId),HttpStatus.OK);
    }
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
    @PreAuthorize("hasAnyRole('"+AppCon.ROLE_NORMAL+"','"+AppCon.ROLE_ADMIN+"')")
    @PostMapping("/update/{orderId}")
    @Operation(summary = "Update order by order Id")
    public ResponseEntity<OrderDTO> updateOrder(
            @RequestBody OrderDTO orderDTO,
            @PathVariable("orderId") String orderId)
    {
        return new ResponseEntity<>(orderService.updateOrder(orderDTO,orderId), HttpStatus.OK);
    }

}
