package rj.com.store.services;

import rj.com.store.datatransferobjects.PageableResponse;
import rj.com.store.datatransferobjects.ProductDTO;
import rj.com.store.enities.Category;

import java.util.Date;

/**
 * Interface for managing product-related operations in the store.
 * This service provides methods to create, update, delete, and retrieve products.
 */
public interface ProductService {

    /**
     * Creates a new product.
     *
     * @param productDTO the product data transfer object containing product details
     * @return the created ProductDTO
     */
    ProductDTO createProduct(ProductDTO productDTO);

    /**
     * Deletes a product by its ID.
     *
     * @param productId the ID of the product to be deleted
     */
    void DeleteProduct(String productId);

    /**
     * Updates an existing product.
     *
     * @param productDTO the product data transfer object containing updated product details
     * @param productId the ID of the product to be updated
     * @return the updated ProductDTO
     */
    ProductDTO updateProduct(ProductDTO productDTO, String productId);

    /**
     * Retrieves a single product by its ID.
     *
     * @param productId the ID of the product to be retrieved
     * @return the ProductDTO of the retrieved product
     */
    ProductDTO getSingleProduct(String productId);

    /**
     * Retrieves all products with pagination and sorting options.
     *
     * @param pageNumber the page number to retrieve
     * @param pageSize the number of products per page
     * @param sortBy the field to sort by
     * @param sortDir the direction of sorting (asc or desc)
     * @return a PageableResponse containing a list of ProductDTOs
     */
    PageableResponse<ProductDTO> getAllProduct(int pageNumber, int pageSize, String sortBy, String sortDir);

    /**
     * Searches for products by title with pagination and sorting options.
     *
     * @param title the title to search for
     * @param pageNumber the page number to retrieve
     * @param pageSize the number of products per page
     * @param sortBy the field to sort by
     * @param sortDir the direction of sorting (asc or desc)
     * @return a PageableResponse containing a list of matching ProductDTOs
     */
    PageableResponse<ProductDTO> searchProduct(String title, int pageNumber, int pageSize, String sortBy, String sortDir);

    /**
     * Retrieves all live products with pagination and sorting options.
     *
     * @param pageNumber the page number to retrieve
     * @param pageSize the number of products per page
     * @param sortBy the field to sort by
     * @param sortDir the direction of sorting (asc or desc)
     * @return a PageableResponse containing a list of live ProductDTOs
     */
    PageableResponse<ProductDTO> getAllProductLive(int pageNumber, int pageSize, String sortBy, String sortDir);

    /**
     * Retrieves all non-live products with pagination and sorting options.
     *
     * @param pageNumber the page number to retrieve
     * @param pageSize the number of products per page
     * @param sortBy the field to sort by
     * @param sortDir the direction of sorting (asc or desc)
     * @return a PageableResponse containing a list of non-live ProductDTOs
     */
    PageableResponse<ProductDTO> getAllProductNotLive(int pageNumber, int pageSize, String sortBy, String sortDir);

    /**
     * Retrieves all products added on a specific date with pagination and sorting options.
     *
     * @param date the date to filter products by
     * @param pageNumber the page number to retrieve
     * @param pageSize the number of products per page
     * @param sortBy the field to sort by
     * @param sortDir the direction of sorting (asc or desc)
     * @return a PageableResponse containing a list of ProductDTOs added on the specified date
     */
    PageableResponse<ProductDTO> getAllProductByDate(Date date, int pageNumber, int pageSize, String sortBy, String sortDir);

    /**
     * Retrieves all products with a specific price with pagination and sorting options.
     *
     * @param price the price to filter products by
     * @param pageNumber the page number to retrieve
     * @param pageSize the number of products per page
     * @param sortBy the field to sort by
     * @param sortDir the direction of sorting (asc or desc)
     * @return a PageableResponse containing a list of ProductDTOs with the specified price
     */
    PageableResponse<ProductDTO> getAllProductByPrice(double price, int pageNumber, int pageSize, String sortBy, String sortDir);

    /**
     * Retrieves all products within a specified price range with pagination and sorting options.
     *
     * @param minPrice the minimum price to filter products by
     * @param maxPrice the maximum price to filter products by
     * @param pageNumber the page number to retrieve
     * @param pageSize the number of products per page
     * @param sortBy the field to sort by
     * @param sortDir the direction of sorting (asc or desc)
     * @return a PageableResponse containing a list of ProductDTOs within the specified price range
     */
    PageableResponse<ProductDTO> getAllProductBetween(double minPrice, double maxPrice, int pageNumber, int pageSize, String sortBy, String sortDir);

    /**
     * Creates a product and associates it with a category.
     *
     * @param productDTO the product data transfer object containing product details
     * @return the created ProductDTO
     */
    ProductDTO createWithCategory(ProductDTO productDTO);

    /**
     * Retrieves all products belonging to a specific category with pagination and sorting options.
     *
     * @param categoryId the ID of the category to filter products by
     * @param pageNumber the page number to retrieve
     * @param pageSize the number of products per page
     * @param sortBy the field to sort by
     * @param sortDir the direction of sorting (asc or desc)
     * @return a PageableResponse containing a list of ProductDTOs in the specified category
     */
    PageableResponse<ProductDTO> getAllProductByCategory(String categoryId, int pageNumber, int pageSize, String sortBy, String sortDir);

    /**
     * Adds a category to a product by their IDs.
     *
     * @param productId the ID of the product to be updated
     * @param categoryId the ID of the category to be added
     * @return the updated ProductDTO
     */
    ProductDTO addCategory(String productId, String categoryId);

    /**
     * Removes a category from a product by their IDs.
     *
     * @param productId the ID of the product to be updated
     * @param categoryId the ID of the category to be removed
     * @return the updated ProductDTO
     */
    ProductDTO removeCategory(String productId, String categoryId);
}
