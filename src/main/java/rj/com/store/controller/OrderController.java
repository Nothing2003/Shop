package rj.com.store.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rj.com.store.datatransferobjects.ApiResponseMessage;
import rj.com.store.datatransferobjects.OrderDTO;
import rj.com.store.datatransferobjects.PageableResponse;
import rj.com.store.helper.AppCon;
import rj.com.store.services.OrderService;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {
    OrderService orderService;
    public OrderController(OrderService orderService){
        this.orderService=orderService;
    }
    @PostMapping("/create/user/{userId}/cart/{cartId}")
    public ResponseEntity<OrderDTO> createOrder(
            @RequestBody OrderDTO orderDTO,
            @PathVariable("userId") String userId,
            @PathVariable("cartId") String cartId){
        return new ResponseEntity<>(orderService.createOrder(orderDTO,userId,cartId), HttpStatus.CREATED);
    }
    @DeleteMapping("/{orderId}")
    public ResponseEntity<ApiResponseMessage> deleteOrder(@PathVariable("orderId") String orderId){
        orderService.removeOrder(orderId);
        return new ResponseEntity<>(ApiResponseMessage.builder()
                .httpStatus(HttpStatus.OK)
                .success(true)
                .massage("Order is successfully removed !!")
                .build(),HttpStatus.OK);
    }
    @GetMapping("{userId}")
    public ResponseEntity<List<OrderDTO>> getAllOrderByUserId(@PathVariable("userId") String userId ){
        return new ResponseEntity<>(orderService.getAllOrderOfUser(userId),HttpStatus.OK);
    }
    @GetMapping
    public ResponseEntity<PageableResponse<OrderDTO>> getAllOrder(
            @RequestParam(value = "pageNumber", defaultValue = AppCon.Page_Number, required = false) int pageNumber,
            @RequestParam(value = "pageSize", defaultValue = AppCon.Page_Size, required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = "orderId", required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppCon.Sort_Dir, required = false) String sortDir
    )
    {
        return new ResponseEntity<>(orderService.getAllOrder(pageNumber,pageSize,sortBy,sortDir),HttpStatus.OK);
    }
    @PostMapping("/update/{orderId}")
    public ResponseEntity<OrderDTO> updateOrder(
            @RequestBody OrderDTO orderDTO,
            @PathVariable("orderId") String orderId)
    {
        return new ResponseEntity<>(orderService.updateOrder(orderDTO,orderId), HttpStatus.OK);
    }

}
