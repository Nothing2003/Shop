package rj.com.store.enities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import jakarta.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "product")
public class Product {
    @Id
    @Column(name = "product_id")
    private String productId;
    @Column(name = "product_title")
    private String title;
    @Column(length = 10000,name = "product_description")
    private String description;
    @Column(name = "product_price")
    private double price;
    @Column(name = "product_discounted_price")
    private double discountedPrice;
    @Column(name = "product_quantity")
    private int quantity;
    @Column(name = "product_added_date")
    private Date addedDate;
    @Column(name = "product_is_live")
    private  boolean live;
    @Column(name = "product_stock")
    private boolean stock;
    private String productImageName;
    // Many-to-Many relationship with join table
    @ManyToMany( mappedBy = "products",fetch = FetchType.EAGER)
    private List<Category> categories = new ArrayList<>();


}
