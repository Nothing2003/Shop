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

/**
 * Controller for handling category-related APIs.
 *
 * This class provides endpoints for managing categories, including creation, updating, deletion,
 * searching, and handling category cover images.
 * Security requirements ensure that operations are protected as per application roles.
 */
@RestController
@RequestMapping("/categories/v1")
@SecurityRequirement(name = "scheme")
@Tag(name = "Category Controller", description = "This is the API for category operations")
public class CategoryController {
    private final Logger logger=LoggerFactory.getLogger(CategoryController.class);
    private final CategoryService categoryService;
    private final FileService fileService;
    @Value("${category.cover.image.path}")
    String imageUploadPath;
    /**
     * Constructs an instance of {@link CategoryController} with the specified dependencies.
     *
     * @param categoryService the service for managing category-related operations
     * @param fileService     the service for managing file-related operations
     */
    public CategoryController(CategoryService categoryService,FileService fileService) {
        this.categoryService = categoryService;
        this.fileService=fileService;
    }
    /**
     * Creates a new category.
     *
     * This endpoint allows the creation of a new category by accepting category details in the request body.
     *
     * @param categoryDTO the details of the new category to be created
     * @return a ResponseEntity containing the created CategoryDTO
     */
    @PostMapping
    @Operation(summary = "Create a category")
    public ResponseEntity<CategoryDTO> createCategory(@Valid @RequestBody CategoryDTO categoryDTO) {
        return new ResponseEntity<>(categoryService.createCategory(categoryDTO), HttpStatus.CREATED);
    }
    /**
     * Updates an existing category by its ID.
     *
     * This endpoint allows updating the details of an existing category using the category ID.
     *
     * @param categoryDTO the new details for the category
     * @param categoryId  the ID of the category to be updated
     * @return a ResponseEntity containing the updated CategoryDTO
     */
    @PutMapping("/{categoryId}")
    @Operation(summary = "Update category by category Id")
    public ResponseEntity<CategoryDTO> updateCategory(@Valid
            @RequestBody CategoryDTO categoryDTO,
            @PathVariable("categoryId") String categoryId
    ) {
        return new ResponseEntity<>(categoryService.updateCategory(categoryDTO, categoryId), HttpStatus.OK);
    }
    /**
     * Deletes a category by its ID.
     *
     * This endpoint allows the deletion of a category using the category ID.
     *
     * @param categoryId the ID of the category to be deleted
     * @return a ResponseEntity containing a message indicating successful deletion
     */
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
    /**
     * Retrieves all categories with pagination and sorting options.
     *
     * This endpoint returns a paginated list of categories with sorting and filtering options.
     *
     * @param pageNumber the page number to retrieve
     * @param pageSize   the number of categories per page
     * @param sortBy     the attribute by which to sort categories
     * @param sortDir    the direction of sorting (asc/desc)
     * @return a ResponseEntity containing a PageableResponse of CategoryDTOs
     */
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
    /**
     * Retrieves a category by its ID.
     *
     * This endpoint allows retrieving a category's details using its ID.
     *
     * @param categoryId the ID of the category to retrieve
     * @return a ResponseEntity containing the CategoryDTO of the requested category
     */
    @GetMapping("/{categoryId}")
    @Operation(summary = "Get category by category")
    public ResponseEntity<CategoryDTO> getSingleCategoryById(@PathVariable("categoryId") String categoryId) {
        return new ResponseEntity<>(categoryService.getCategoryById(categoryId), HttpStatus.OK);
    }
    /**
     * Searches for categories based on a keyword.
     *
     * This endpoint allows searching for categories that match the provided keyword.
     *
     * @param keyword    the keyword to search for
     * @param pageNumber the page number to retrieve
     * @param pageSize   the number of categories per page
     * @param sortBy     the attribute by which to sort categories
     * @param sortDir    the direction of sorting (asc/desc)
     * @return a ResponseEntity containing a PageableResponse of CategoryDTOs
     */

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

    /**
     * Uploads an image for a specific category.
     *
     * This endpoint allows uploading a cover image for a category.
     *
     * @param categoryId the ID of the category to upload the image for
     * @param image      the MultipartFile representing the image to upload
     * @return a ResponseEntity containing the uploaded image details
     * @throws IOException if an error occurs during image upload
     */
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

    /**
     * Serves the cover image for a specific category.
     *
     * This endpoint allows serving the cover image of a category by returning the image file.
     *
     * @param categoryId the ID of the category to retrieve the image for
     * @param response   the HttpServletResponse to write the image to
     * @throws IOException if an error occurs during image retrieval
     */
    @GetMapping("/image/{categoryId}")
    public void serveUserImage(@PathVariable("categoryId") String categoryId, HttpServletResponse response) throws IOException {
        CategoryDTO categoryDTO=categoryService.getCategoryById(categoryId);
        logger.info("Category image name {}", categoryDTO.getCoverImage());
        InputStream resource = fileService.getResource(imageUploadPath,  categoryDTO.getCoverImage());
        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        StreamUtils.copy(resource, response.getOutputStream());
    }
}
