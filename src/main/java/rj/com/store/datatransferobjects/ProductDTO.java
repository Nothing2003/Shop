package rj.com.store.datatransferobjects;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.w3c.dom.stylesheets.LinkStyle;

import java.util.Date;
import java.util.List;

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
    private List<CategoryDTO> categoryDTO;
}
