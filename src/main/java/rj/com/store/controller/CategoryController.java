package rj.com.store.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import rj.com.store.datatransferobjects.*;
import rj.com.store.helper.AppCon;
import rj.com.store.services.CategoryService;
import rj.com.store.services.FileService;
import java.io.IOException;
import java.io.InputStream;
@RestController
@RequestMapping("/categories/v1")
@SecurityRequirement(name = "scheme")
@Tag(name = "Category Controller ",description = "This is cart Api for category operation")
public class CategoryController {
    private final Logger logger=LoggerFactory.getLogger(CategoryController.class);
    private final CategoryService categoryService;
    private final FileService fileService;
    @Value("${category.cover.image.path}")
    String imageUploadPath;
    public CategoryController(CategoryService categoryService,FileService fileService) {
        this.categoryService = categoryService;
        this.fileService=fileService;
    }
    //create
    @PostMapping
    @Operation(summary = "Create a category")
    public ResponseEntity<CategoryDTO> createCategory(@Valid @RequestBody CategoryDTO categoryDTO) {
        return new ResponseEntity<>(categoryService.createCategory(categoryDTO), HttpStatus.CREATED);
    }

    //update
    @PutMapping("/{categoryId}")
    @Operation(summary = "Update category by category Id")
    public ResponseEntity<CategoryDTO> updateCategory(@Valid
            @RequestBody CategoryDTO categoryDTO,
            @PathVariable("categoryId") String categoryId
    ) {
        return new ResponseEntity<>(categoryService.updateCategory(categoryDTO, categoryId), HttpStatus.OK);
    }

    //delete
    @DeleteMapping("/{categoryId}")
    @Operation(summary = "Delete category by category Id")
    public ResponseEntity<ApiResponseMessage> deleteCategory(@PathVariable("categoryId") String categoryId) {
        categoryService.deleteCategory(categoryId);
        return new ResponseEntity<>(
                ApiResponseMessage.builder()
                        .massage("Category is deleted successfully!")
                        .success(true)
                        .httpStatus(HttpStatus.OK)
                        .build(),
                HttpStatus.OK
        );
    }

    //get all
    @GetMapping
    @Operation(summary = "Get all category")
    public ResponseEntity<PageableResponse<CategoryDTO>> getAllCategory(
            @RequestParam(value = "pageNumber", defaultValue = AppCon.Page_Number, required = false) int pageNumber,
            @RequestParam(value = "pageSize", defaultValue = AppCon.Page_Size, required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue ="Title", required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppCon.Sort_Dir, required = false) String sortDir
    ) {
        return new ResponseEntity<>(

                categoryService.getAllCategory(pageNumber, pageSize, sortBy, sortDir),
                HttpStatus.OK
        );
    }

    //get single
    @GetMapping("/{categoryId}")
    @Operation(summary = "Get category by category")
    public ResponseEntity<CategoryDTO> getSingleCategoryById(@PathVariable("categoryId") String categoryId) {
        return new ResponseEntity<>(categoryService.getCategoryById(categoryId), HttpStatus.OK);
    }

    @GetMapping("/search/{keyword}")
    @Operation(summary ="Search category by keyword")
    public ResponseEntity<PageableResponse<CategoryDTO>> searchUser(
            @PathVariable("keyword") String keyword,
            @RequestParam(value = "pageNumber", defaultValue = AppCon.Page_Number, required = false) int pageNumber,
            @RequestParam(value = "pageSize", defaultValue = AppCon.Page_Size, required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = "Title", required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppCon.Sort_Dir, required = false) String sortDir) {
        return new ResponseEntity<>(
               categoryService.searchCategory(keyword, pageNumber, pageSize, sortBy, sortDir),
                HttpStatus.OK);
    }

    //upload image
    @PostMapping("/image/{categoryId}")

    public ResponseEntity<ImageResponse> uploadUserImage(@PathVariable("categoryId") String categoryId,
                                                         @RequestParam("categoryCoverImage") MultipartFile image) throws IOException {
        String imageName = fileService.uploadImage(image, imageUploadPath);
        logger.info("Image path name {}", imageName);
        CategoryDTO categoryDTO=categoryService.getCategoryById(categoryId);
        categoryDTO.setCoverImage(imageName);
        categoryService.updateCategory(categoryDTO,categoryId);
        return new ResponseEntity<>(
                ImageResponse
                        .builder()
                        .imageName(imageName)
                        .massage("Image uploaded successfully")
                        .success(true)
                        .httpStatus(HttpStatus.CREATED)
                        .build(),
                HttpStatus.CREATED
        );
    }

    //serve Category Image
    @GetMapping("/image/{categoryId}")
    public void serveUserImage(@PathVariable("categoryId") String categoryId, HttpServletResponse response) throws IOException {
        CategoryDTO categoryDTO=categoryService.getCategoryById(categoryId);
        logger.info("Category image name {}", categoryDTO.getCoverImage());
        InputStream resource = fileService.getResource(imageUploadPath,  categoryDTO.getCoverImage());
        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        StreamUtils.copy(resource, response.getOutputStream());
    }
}
