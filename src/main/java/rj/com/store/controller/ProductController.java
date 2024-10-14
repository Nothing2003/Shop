package rj.com.store.controller;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;
import rj.com.store.datatransferobjects.ApiResponseMessage;
import rj.com.store.datatransferobjects.PageableResponse;
import rj.com.store.datatransferobjects.ProductDTO;
import rj.com.store.exceptions.ResourceNotFoundException;
import rj.com.store.helper.AppCon;
import rj.com.store.services.ProductService;
import java.util.Date;
/**
 * Controller for handling product-related APIs.
 *
 * This class provides endpoints for creating, updating, deleting,
 * and fetching products. The operations are secured based on roles.
 */
@RestController
@RequestMapping("/products/v1")
@SecurityRequirement(name = "scheme")
@Tag(name = "Product Controller", description = "This API handles product operations such as creating, updating, fetching, and deleting products.")

public class ProductController {
    ProductService productService;

    /**
     * Constructor to initialize the ProductService.
     *
     * @param productService the service handling business logic for products
     */
    public  ProductController(ProductService productService){
        this.productService=productService;
    }
    /**
     * Creates a new product.
     *
     * This operation is accessible to users with ADMIN role.
     *
     * @param productDTO the details of the product to be created
     * @return a ResponseEntity containing the created ProductDTO
     */
    @PostMapping
    @Operation(summary = " Create a product")
    public ResponseEntity<ProductDTO> createProduct(@RequestBody ProductDTO productDTO)
    {
        return new ResponseEntity<>(productService.createProduct(productDTO), HttpStatus.CREATED);
    }
    /**
     * Deletes a product by its ID.
     *
     * This operation is restricted to ADMIN role.
     *
     * @param productId the ID of the product to be deleted
     * @return a ResponseEntity with a success message
     */
    @DeleteMapping("/{productId}")
    @Operation(summary = "Delete a product")
    public ResponseEntity<ApiResponseMessage> deleteProduct(@PathVariable("productId") String productId)
    {
        productService.DeleteProduct(productId);
        return new ResponseEntity<>(ApiResponseMessage.builder()
                .success(true)
                .massage("Product successfully deleted!")
                .httpStatus(HttpStatus.ACCEPTED)
                .build(),
                HttpStatus.ACCEPTED);
    }
    /**
     * Updates an existing product by its ID.
     *
     * This operation is accessible to users with ADMIN role.
     *
     * @param productDTO the updated product details
     * @param productId the ID of the product to be updated
     * @return a ResponseEntity containing the updated ProductDTO
     */
    @PutMapping("/{productId}")
    @Operation(summary = "Update a product")
    public   ResponseEntity<ProductDTO> updateProduct(@PathVariable("productId") String productId ,@RequestBody ProductDTO productDTO)
    {
        return new ResponseEntity<>(productService.updateProduct(productDTO,productId), HttpStatus.ACCEPTED);
    }
    /**
     * Fetches a product by its ID.
     *
     * This operation is accessible to users with ADMIN or NORMAL roles.
     *
     * @param productId the ID of the product to be fetched
     * @return a ResponseEntity containing the ProductDTO
     */
    @GetMapping("/{productId}")
    @Operation(summary = " Get single product by product Id")
    public ResponseEntity<ProductDTO> getSingleProduct(@PathVariable("productId") String productId)
    {
        productService.getSingleProduct(productId);
        return new ResponseEntity<>(productService.getSingleProduct(productId),HttpStatus.OK);
    }
    /**
     * Fetches all products with pagination.
     *
     * This operation is accessible to users with ADMIN role.
     *
     * @param pageNumber the page number for pagination
     * @param pageSize the number of items per page
     * @param sortBy the attribute to sort by
     * @param sortDir the sort direction (ASC/DESC)
     * @return a ResponseEntity containing the paginated response of ProductDTOs
     */
    @GetMapping
    @Operation(summary = "Get all products")
    public   ResponseEntity<PageableResponse<ProductDTO>> getAllProduct(
            @RequestParam(value = "pageNumber",defaultValue = AppCon.Page_Number,required = false) int pageNumber,
            @RequestParam(value = "pageSize",defaultValue = AppCon.Page_Size,required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue="title",required = false) String sortBy ,
            @RequestParam(value = "sortDir", defaultValue=AppCon.Sort_Dir,required = false) String sortDir
    )
    {
        return new ResponseEntity<>(productService.getAllProduct(pageNumber,pageSize, sortBy,sortDir),HttpStatus.OK);
    }
    /**
     * Searches for products by keyword.
     *
     * This operation is accessible to users with ADMIN or NORMAL roles.
     *
     * @param keyword the keyword to search for
     * @return a ResponseEntity containing a list of matching ProductDTOs
     */
    @GetMapping("/search/{keyword}")
    @Operation(summary = "Search product by keyword")
    public ResponseEntity<PageableResponse<ProductDTO>> searchProduct(
            @PathVariable("keyword") String keyword,
            @RequestParam(value = "pageNumber",defaultValue = AppCon.Page_Number,required = false) int pageNumber,
            @RequestParam(value = "pageSize",defaultValue = AppCon.Page_Size,required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue="title",required = false) String sortBy ,
            @RequestParam(value = "sortDir", defaultValue=AppCon.Sort_Dir,required = false) String sortDir
    )
    {
        return new ResponseEntity<>(productService.searchProduct(keyword,pageNumber,pageSize, sortBy,sortDir),HttpStatus.OK);
    }
    /**
     * Fetches all available (live) products with pagination.
     *
     * This operation is accessible to users with ADMIN or NORMAL roles.
     *
     * @param pageNumber the page number for pagination
     * @param pageSize the number of items per page
     * @param sortBy the attribute to sort by
     * @param sortDir the sort direction (ASC/DESC)
     * @return a ResponseEntity containing the paginated response of available ProductDTOs
     */
    @GetMapping("/available")
    @Operation(summary = "Get all available products ")
    public ResponseEntity<PageableResponse<ProductDTO>> getProductIsLive(
            @RequestParam(value = "pageNumber",defaultValue = AppCon.Page_Number,required = false) int pageNumber,
            @RequestParam(value = "pageSize",defaultValue = AppCon.Page_Size,required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue="title",required = false) String sortBy ,
            @RequestParam(value = "sortDir", defaultValue=AppCon.Sort_Dir,required = false) String sortDir
    )
    {
       return new ResponseEntity<>(productService.getAllProductLive(pageNumber,pageSize, sortBy,sortDir),HttpStatus.OK);
    }
    /**
     * Fetches all unavailable (live=false) products with pagination.
     *
     * This operation is accessible to users with ADMIN or NORMAL roles.
     *
     * @param pageNumber the page number for pagination
     * @param pageSize the number of items per page
     * @param sortBy the attribute to sort by
     * @param sortDir the sort direction (ASC/DESC)
     * @return a ResponseEntity containing the paginated response of available ProductDTOs
     */
    @GetMapping("/not/available")
    @Operation(summary = "Get all unavailable products ")
    public ResponseEntity<PageableResponse<ProductDTO>> getProductIsNotLive(
            @RequestParam(value = "pageNumber",defaultValue = AppCon.Page_Number,required = false) int pageNumber,
            @RequestParam(value = "pageSize",defaultValue = AppCon.Page_Size,required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue="title",required = false) String sortBy ,
            @RequestParam(value = "sortDir", defaultValue=AppCon.Sort_Dir,required = false) String sortDir
    )
    {
        return new ResponseEntity<>(productService.getAllProductNotLive(pageNumber,pageSize, sortBy,sortDir),HttpStatus.OK);
    }
    /**
     * Fetches all products created on a specific date with pagination.
     *
     * This operation is accessible to users with ADMIN or NORMAL roles.
     *
     * @param date the date to filter products
     * @param pageNumber the page number for pagination
     * @param pageSize the number of items per page
     * @param sortBy the attribute to sort by
     * @param sortDir the sort direction (ASC/DESC)
     * @return a ResponseEntity containing the paginated response of ProductDTOs
     */
    @GetMapping("/date")
    @Operation(summary = "Get products by date")
    public ResponseEntity<PageableResponse<ProductDTO>> getProductByDate(
            @RequestBody Date date,
            @RequestParam(value = "pageNumber",defaultValue = AppCon.Page_Number,required = false) int pageNumber,
            @RequestParam(value = "pageSize",defaultValue = AppCon.Page_Size,required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue="title",required = false) String sortBy ,
            @RequestParam(value = "sortDir", defaultValue=AppCon.Sort_Dir,required = false) String sortDir
    )
    {
        return new ResponseEntity<>(productService.getAllProductByDate(date,pageNumber,pageSize,sortBy,sortDir),HttpStatus.OK);
    }
    /**
     * Fetches all products at a specific price with pagination.
     *
     * This operation is accessible to users with ADMIN or NORMAL roles.
     *
     * @param price the price to filter products
     * @param pageNumber the page number for pagination
     * @param pageSize the number of items per page
     * @param sortBy the attribute to sort by
     * @param sortDir the sort direction (ASC/DESC)
     * @return a ResponseEntity containing the paginated response of ProductDTOs
     */
    @GetMapping("/price")
    @Operation(summary = "Get all products by price ")
    public ResponseEntity<PageableResponse<ProductDTO>> getProductByPrice(
            @RequestParam("price") double price,
            @RequestParam(value = "pageNumber",defaultValue = AppCon.Page_Number,required = false) int pageNumber,
            @RequestParam(value = "pageSize",defaultValue = AppCon.Page_Size,required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue="title",required = false) String sortBy ,
            @RequestParam(value = "sortDir", defaultValue=AppCon.Sort_Dir,required = false) String sortDir
    )
    {
        return new ResponseEntity<>(productService.getAllProductByPrice(price,pageNumber,pageSize,sortBy,sortDir),HttpStatus.OK);
    }

    /**
     * Fetches all products at a specific price with pagination.
     *
     * @param maxPrice the highest price on who also be performed
     * @param minPrice the lowest price on who also be performed
     * @param pageNumber the page number for pagination
     * @param pageSize the number of items per page
     * @param sortBy the attribute to sort by
     * @param sortDir the sort direction (ASC/DESC)
     * @return a ResponseEntity containing the paginated response of ProductDTOs
     */
    @GetMapping("/price/limit")
    @Operation(summary = "Get all product between minimum and maximum price ")
    public ResponseEntity<PageableResponse<ProductDTO>> getProductBetweenPrice(
            @RequestParam("min-price") double minPrice,
            @RequestParam("max-price") double maxPrice,
            @RequestParam(value = "pageNumber",defaultValue = AppCon.Page_Number,required = false) int pageNumber,
            @RequestParam(value = "pageSize",defaultValue = AppCon.Page_Size,required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue="title",required = false) String sortBy ,
            @RequestParam(value = "sortDir", defaultValue=AppCon.Sort_Dir,required = false) String sortDir
    )
    {
        return new ResponseEntity<>(productService.getAllProductBetween(minPrice,maxPrice,pageNumber,pageSize,sortBy,sortDir),HttpStatus.OK);
    }
    /**
     * Creates a new product along with its associated category.
     *
     * @param productDTO the details of the product to be created
     * @return a ResponseEntity containing the created ProductDTO
     */
    @PostMapping("/with-category")
    @Operation(summary = "User to create product with category ")
    public ResponseEntity<ProductDTO> createProductWithCategory(@RequestBody ProductDTO productDTO){
        return new ResponseEntity<>(productService.createWithCategory(productDTO),HttpStatus.CREATED);
    }
    /**
     * Retrieves all products associated with a specific category ID.
     *
     * This operation is accessible to all users.
     *
     * @param categoryId the ID of the category to filter products by
     * @param pageNumber the page number for pagination
     * @param pageSize the number of items per page
     * @param sortBy the attribute to sort by
     * @param sortDir the sort direction (ASC/DESC)
     * @return a ResponseEntity containing the paginated response of ProductDTOs
     */
    @GetMapping("/category-by-product/{categoryId}")
    @Operation(summary = "User to get all products by category Id ")
    public ResponseEntity<PageableResponse<ProductDTO>> getProductByCategory(
            @PathVariable("categoryId") String categoryId,
            @RequestParam(value = "pageNumber",defaultValue = AppCon.Page_Number,required = false) int pageNumber,
            @RequestParam(value = "pageSize",defaultValue = AppCon.Page_Size,required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue="title",required = false) String sortBy ,
            @RequestParam(value = "sortDir", defaultValue=AppCon.Sort_Dir,required = false) String sortDir
    )
    {
        return new ResponseEntity<>(
                productService.getAllProductByCategory(categoryId,pageNumber,pageSize,sortBy,sortDir)
                ,HttpStatus.OK);
    }
    /**
     * Adds a category to a product by their respective IDs.
     *
     * This operation is accessible to users with appropriate roles.
     *
     * @param productId the ID of the product to which the category will be added
     * @param categoryId the ID of the category to be added to the product
     * @return a ResponseEntity containing the updated ProductDTO
     */
    @PutMapping("/add-category/product/{productId}/category/{categoryId}")
    @Operation(summary = "Products by category Id ")
    public ResponseEntity<ProductDTO> addProductCategory(
            @PathVariable("categoryId") String categoryId,
            @PathVariable("productId") String productId
    )
    {
        return new ResponseEntity<>(productService.addCategory(productId,categoryId),HttpStatus.OK);
    }
    /**
     * Removes a category from a product by their respective IDs.
     *
     * This operation is accessible to users with appropriate roles.
     *
     * @param productId the ID of the product from which the category will be removed
     * @param categoryId the ID of the category to be removed from the product
     * @return a ResponseEntity containing the updated ProductDTO
     */
    @PutMapping("/remove-category/product/{productId}/category/{categoryId}")
    @Operation(summary = "Products by category Id ")
    public ResponseEntity<ProductDTO> removeProductCategory(
            @PathVariable("categoryId") String categoryId,
            @PathVariable("productId") String productId
    )
    {
        return new ResponseEntity<>(productService.removeCategory(productId,categoryId),HttpStatus.OK);
    }
}
