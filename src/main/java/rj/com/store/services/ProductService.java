package rj.com.store.services;

import rj.com.store.datatransferobjects.PageableResponse;
import rj.com.store.datatransferobjects.ProductDTO;
import rj.com.store.enities.Category;

import java.util.Date;

public interface ProductService {
//    create
    ProductDTO createProduct(ProductDTO productDTO);
//    delete
    void DeleteProduct(String productId);
//    update
    ProductDTO updateProduct(ProductDTO productDTO,String productId);
//    get Single Product
    ProductDTO getSingleProduct(String productId);
//    get All Product
    PageableResponse<ProductDTO> getAllProduct(int pageNumber, int pageSize, String sortBy, String sortDir);
//    search
    PageableResponse<ProductDTO> searchProduct(String title,int pageNumber, int pageSize, String sortBy, String sortDir);
//    get All Product Live
    PageableResponse<ProductDTO> getAllProductLive(int pageNumber, int pageSize, String sortBy, String sortDir);
//    get All Product Not Live
    PageableResponse<ProductDTO> getAllProductNotLive(int pageNumber, int pageSize, String sortBy, String sortDir);
//    get All Product by date
    PageableResponse<ProductDTO> getAllProductByDate(Date date,int pageNumber, int pageSize, String sortBy, String sortDir);
//    get all product by price
    PageableResponse<ProductDTO> getAllProductByPrice(double price,int pageNumber, int pageSize, String sortBy, String sortDir);
//    get all product between price
    PageableResponse<ProductDTO> getAllProductBetween(double minPrice,double maxPrice,int pageNumber, int pageSize, String sortBy, String sortDir);
    //create product with category
    ProductDTO createWithCategory (ProductDTO productDTO,String categoryId);
    //get product by CategoryId
    PageableResponse<ProductDTO> getAllProductByCategory(String categoryId, int pageNumber, int pageSize, String sortBy, String sortDir);
    //update product with category
    ProductDTO updateCategory(String productId,String categoryId);

}
