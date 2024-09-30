package rj.com.store.enities;

import lombok.*;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@AllArgsConstructor@NoArgsConstructor@Builder@Getter@Setter
@Entity
@Table(name = "order_table")
public class Order {
    @Id
    private String orderId;
    private String orderStatus;
    private String paymentStatus;
    private double totalAmount;
    @Column(length = 1000)
    private String billingAddress;
    private String billingPhone;
    private String billingName;
    private Date orderDate;
    private Date deliveredDate;
    @ManyToOne(fetch = FetchType.EAGER)
    private User user;
    @OneToMany(mappedBy ="order",fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    private List<OrderItem> orderItems=new ArrayList<>();
}
