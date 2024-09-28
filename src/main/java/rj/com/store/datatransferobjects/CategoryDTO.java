package rj.com.store.datatransferobjects;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import rj.com.store.validate.ImageNameValid;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDTO {
    private String categoryId;
    @NotBlank(message = "title required")
    @Size(min = 3,message = "Minimum 3 charter required")
    private String title;
//    @ImageNameValid(message = "Image Required")
    private String coverImage;
    @NotBlank(message = "description is required")
    private String description;
    private List<ProductDTO> productDTO;
}
