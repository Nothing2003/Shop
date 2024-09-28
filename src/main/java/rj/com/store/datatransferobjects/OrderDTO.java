package rj.com.store.datatransferobjects;

import lombok.*;
import rj.com.store.enities.OrderItem;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
@NoArgsConstructor
@Builder
@AllArgsConstructor
@Setter
@Getter
public class OrderDTO {
    private String orderId;
    private String orderStatus="PENDING";
    private String paymentStatus="NOT_PAID";
    private double totalAmount;
    private String billingAddress;
    private String billingPhone;
    private String billingName;
    private Date orderDate=new Date();
    private Date deliveredDate;
    private List<OrderItemDTO> orderItems=new ArrayList<>();
}
