package rj.com.store.controller;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rj.com.store.datatransferobjects.ApiResponseMessage;
import rj.com.store.datatransferobjects.PageableResponse;
import rj.com.store.datatransferobjects.ProductDTO;
import rj.com.store.helper.AppCon;
import rj.com.store.services.ProductService;
import java.util.Date;

@RestController
@RequestMapping("/products")
public class ProductController {
    ProductService productService;
    public  ProductController(ProductService productService){
        this.productService=productService;
    }
    //    create
    @PostMapping
    public ResponseEntity<ProductDTO> createProduct(@RequestBody ProductDTO productDTO)
    {
        return new ResponseEntity<>(productService.createProduct(productDTO), HttpStatus.CREATED);
    }
    //    delete
    @DeleteMapping("/{productId}")
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
    //    update
    @PutMapping("/{productId}")
    public   ResponseEntity<ProductDTO> updateProduct(@PathVariable("productId") String productId ,@RequestBody ProductDTO productDTO)
    {
        return new ResponseEntity<>(productService.updateProduct(productDTO,productId), HttpStatus.ACCEPTED);
    }
    //    get Single Product
    @GetMapping("/{productId}")
    public ResponseEntity<ProductDTO> getSingleProduct(@PathVariable("productId") String productId)
    {
        productService.getSingleProduct(productId);
        return new ResponseEntity<>(productService.getSingleProduct(productId),HttpStatus.OK);
    }
    //    get All Product
    @GetMapping
    public   ResponseEntity<PageableResponse<ProductDTO>> getAllProduct(
            @RequestParam(value = "pageNumber",defaultValue = AppCon.Page_Number,required = false) int pageNumber,
            @RequestParam(value = "pageSize",defaultValue = AppCon.Page_Size,required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue="title",required = false) String sortBy ,
            @RequestParam(value = "sortDir", defaultValue=AppCon.Sort_Dir,required = false) String sortDir
    )
    {
        return new ResponseEntity<>(productService.getAllProduct(pageNumber,pageSize, sortBy,sortDir),HttpStatus.OK);
    }
    //    search
    @GetMapping("/search/{keyword}")
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
    //    get All Product Live
    @GetMapping("/available")
    public ResponseEntity<PageableResponse<ProductDTO>> getProductIsLive(
            @RequestParam(value = "pageNumber",defaultValue = AppCon.Page_Number,required = false) int pageNumber,
            @RequestParam(value = "pageSize",defaultValue = AppCon.Page_Size,required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue="title",required = false) String sortBy ,
            @RequestParam(value = "sortDir", defaultValue=AppCon.Sort_Dir,required = false) String sortDir
    )
    {
       return new ResponseEntity<>(productService.getAllProductLive(pageNumber,pageSize, sortBy,sortDir),HttpStatus.OK);
    }
    //    get All Product Not Live
    @GetMapping("/not/available")
    public ResponseEntity<PageableResponse<ProductDTO>> getProductIsNotLive(
            @RequestParam(value = "pageNumber",defaultValue = AppCon.Page_Number,required = false) int pageNumber,
            @RequestParam(value = "pageSize",defaultValue = AppCon.Page_Size,required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue="title",required = false) String sortBy ,
            @RequestParam(value = "sortDir", defaultValue=AppCon.Sort_Dir,required = false) String sortDir
    )
    {
        return new ResponseEntity<>(productService.getAllProductNotLive(pageNumber,pageSize, sortBy,sortDir),HttpStatus.OK);
    }
    //    get All Product by date
    @GetMapping("/date")
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
    //    get all product by price
    @GetMapping("/price")
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

    //    get all product between price
    @GetMapping("/price/limit")
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
    //product with category
    @PostMapping("/with-category/{categoryId}")
    public ResponseEntity<ProductDTO> createProductWithCategory(@PathVariable("categoryId") String categoryId,@RequestBody ProductDTO productDTO){
        return new ResponseEntity<>(productService.createWithCategory(productDTO,categoryId),HttpStatus.CREATED);
    }
    //get all product by category
    @GetMapping("/category-by-product/{categoryId}")
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
    //update product by category
    @PutMapping("/update-category/product/{productId}/category/{categoryId}")
    public ResponseEntity<ProductDTO> updateProductCategory(
            @PathVariable("categoryId") String categoryId,
            @PathVariable("productId") String productId
    )
    {
        return new ResponseEntity<>(productService.updateCategory(productId,categoryId),HttpStatus.OK);
    }
}
