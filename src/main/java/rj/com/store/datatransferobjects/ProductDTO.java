package rj.com.store.datatransferobjects;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.validation.constraints.NotBlank;
import java.util.Date;
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
public class ProductDTO {
    private String productId;
    @NotBlank
    private String title;
    @NotBlank
    private String description;
    @NotBlank
    private double price;
    @NotBlank
    private double discountedPrice;
    @NotBlank
    private int quantity;
    @NotBlank
    private Date addedDate;
    private  boolean live;
    private boolean stock;
    private String productImageName;
    private CategoryDTO categoryDTO;
}
