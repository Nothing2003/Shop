package rj.com.store.datatransferobjects;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
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
    private String coverImage;
    @NotBlank(message = "description is required")
    private String description;
    private List<ProductDTO> productDTO;
}
