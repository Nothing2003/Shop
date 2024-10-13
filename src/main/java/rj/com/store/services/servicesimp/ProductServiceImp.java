package rj.com.store.services.servicesimp;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import rj.com.store.datatransferobjects.PageableResponse;
import rj.com.store.datatransferobjects.ProductDTO;
import rj.com.store.enities.Category;
import rj.com.store.enities.Product;
import rj.com.store.exceptions.ResourceNotFoundException;
import rj.com.store.helper.Helper;
import rj.com.store.repositories.CategoryRepository;
import rj.com.store.repositories.ProductRepository;
import rj.com.store.services.ImageServiceInCloud;
import rj.com.store.services.ProductService;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class ProductServiceImp implements ProductService {
    // Repository for performing CRUD operations on products.
    private final ProductRepository productRepository;
    // Mapper for converting between Product and ProductDTO objects.
    private final ModelMapper mapper;
    // Service for handling image operations in the cloud.
    private final ImageServiceInCloud imageServiceInCloud;
    // Repository for performing CRUD operations on categories.
    private final CategoryRepository categoryRepository;
    // Logger for logging information and errors related to product services.
    private final Logger logger= LoggerFactory.getLogger(ProductServiceImp.class);
    /**
     * Constructs a new instance of ProductServiceImp with the specified dependencies.
     *
     * @param productRepository the repository for product data operations
     * @param mapper            the model mapper for converting between Product and ProductDTO
     * @param imageServiceInCloud the service for handling images in the cloud
     * @param categoryRepository the repository for category data operations
     */
    public ProductServiceImp(ProductRepository productRepository, ModelMapper mapper, ImageServiceInCloud imageServiceInCloud, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.mapper = mapper;
        this.imageServiceInCloud = imageServiceInCloud;
        this.categoryRepository = categoryRepository;
    }
    /**
     * Creates a new product based on the provided ProductDTO.
     *
     * This method generates a unique product ID and sets the current date
     * as the added date before saving the product to the repository.
     *
     * @param productDTO the Data Transfer Object containing product details
     * @return the created ProductDTO after being saved in the repository
     */
    @Override
    public ProductDTO createProduct(ProductDTO productDTO) {
        // Generate a unique product ID and set the current date
       productDTO.setProductId(UUID.randomUUID().toString());
       productDTO.setAddedDate(new Date());
        // Log the addition of a product
       logger.info("Product Add : {}",1);
        // Save the product entity and return the corresponding DTO
       return mapper.map(
                productRepository.save(
                        mapper.map(productDTO,Product.class)
                )
               ,ProductDTO.class
       );
    }
    /**
     * Deletes a product from the repository based on the provided product ID.
     * This method retrieves the product from the repository and deletes the associated image
     * from the cloud storage before removing the product from the repository.
     * If the product with the specified ID does not exist, a ResourceNotFoundException is thrown.
     * @param productId the ID of the product to be deleted
     * @throws ResourceNotFoundException if the product with the specified ID is not found
     */
    @Override
    public void DeleteProduct(String productId) {
        // Retrieve the product by its ID or throw an exception if not found
       Product product= productRepository.findById(productId).orElseThrow(()->new ResourceNotFoundException("Product is not found"));
        // Delete the associated image from cloud storage
       imageServiceInCloud.deleteImage(product.getProductImageName());
        // Remove the product from the repository
       productRepository.delete(product);
        // Log the deletion of the product
       logger.info("Product Delete : {}",1);
    }
    /**
     * Updates the details of an existing product based on the provided product ID and updated product data.
     *
     * This method retrieves the product from the repository using the provided product ID.
     * If the product exists, it updates its fields with the values from the provided ProductDTO.
     * If the product image name has changed, it deletes the old image from cloud storage
     * before setting the new image name. If the product with the specified ID is not found,
     * a ResourceNotFoundException is thrown.
     *
     * @param productDTO the new product data to update the existing product
     * @param productId the ID of the product to be updated
     * @return the updated ProductDTO
     * @throws ResourceNotFoundException if the product with the specified ID is not found
     */
    @Override
    public ProductDTO updateProduct(ProductDTO productDTO, String productId) {
        // Retrieve the product by its ID or throw an exception if not found
      Product product=productRepository.findById(productId).orElseThrow(()->new ResourceNotFoundException("Product Not Found"));
        // Update product fields with data from the provided productDTO
      product.setTitle(productDTO.getTitle());
      product.setLive(productDTO.isLive());
      product.setDescription(productDTO.getDescription());
      product.setQuantity(productDTO.getQuantity());
      product.setPrice(productDTO.getPrice());
      product.setStock(productDTO.isStock());
      product.setDiscountedPrice(productDTO.getDiscountedPrice());
        // Check if the product image name has changed
      if (product.getProductImageName().equalsIgnoreCase(productDTO.getProductId())){
          product.setProductImageName(productDTO.getProductImageName());
      }else{
          // Delete the old image if it has changed
          imageServiceInCloud.deleteImage(product.getProductImageName());
          product.setProductImageName(productDTO.getProductImageName());
      }
        // Log the update action
      logger.info("Product Update : {}",1);
        // Save the updated product and return the updated ProductDTO
      return mapper.map(productRepository.save(product),ProductDTO.class);
    }
    /**
     * Retrieves a single product based on the specified product ID.
     *
     * This method searches for the product in the repository using the provided product ID.
     * If the product exists, it maps the product entity to a ProductDTO and returns it.
     * If the product with the specified ID is not found, a ResourceNotFoundException is thrown.
     *
     * @param productId the ID of the product to retrieve
     * @return the ProductDTO corresponding to the retrieved product
     * @throws ResourceNotFoundException if the product with the specified ID is not found
     */
    @Override
    public ProductDTO getSingleProduct(String productId) {
        // Retrieve the product by its ID or throw an exception if not found
        Product product=productRepository.findById(productId).orElseThrow(()->new ResourceNotFoundException("Product not found"));
        // Map the retrieved product entity to a ProductDTO and return it
        return mapper.map(product,ProductDTO.class);
    }
    /**
     * Retrieves a paginated list of all products, sorted by the specified criteria.
     *
     * This method fetches a page of products from the repository based on the given
     * page number and size. The products are sorted in either ascending or descending
     * order based on the specified sort field.
     *
     * @param pageNumber the page number to retrieve (zero-based index)
     * @param pageSize   the number of products per page
     * @param sortBy     the field by which to sort the products
     * @param sortDir    the direction of sorting, either "asc" for ascending or "desc" for descending
     * @return a PageableResponse containing the paginated list of ProductDTOs
     */
    @Override
    public PageableResponse<ProductDTO> getAllProduct(int pageNumber, int pageSize, String sortBy, String sortDir) {
        // Fetch the products with pagination and sorting
      Page<Product> page= productRepository.findAll(PageRequest.of(pageNumber,pageSize,
                (sortDir.equalsIgnoreCase("desc"))?
                        (Sort.by(sortBy).descending())
                        :
                        (Sort.by(sortBy).ascending())));
        // Return the pageable response
        return Helper.getPageableResponse(page,ProductDTO.class);
    }
    /**
     * Searches for products by their title, returning a paginated and sorted list of matching products.
     * This method retrieves a page of products that contain the specified title in their title field.
     * The results can be sorted by a specified field in either ascending or descending order.
     *
     * @param title     the title or partial title to search for
     * @param pageNumber the page number to retrieve (zero-based index)
     * @param pageSize   the number of products per page
     * @param sortBy     the field by which to sort the results
     * @param sortDir    the direction of sorting, either "asc" for ascending or "desc" for descending
     * @return a PageableResponse containing the paginated list of matching ProductDTOs
     * @throws ResourceNotFoundException if no products matching the search criteria are found
     */
    @Override
    public PageableResponse<ProductDTO> searchProduct(String title,int pageNumber, int pageSize, String sortBy, String sortDir) {
        // Fetch the products that contain the specified title, with pagination and sorting
     Page<Product>page= productRepository.findByTitleContaining(title,PageRequest.of(pageNumber,pageSize,
                (sortDir.equalsIgnoreCase("desc"))?
                        (Sort.by(sortBy).descending())
                        :
                        (Sort.by(sortBy).ascending()))).orElseThrow(()->new ResourceNotFoundException("Products not found exception"));
        // Return the pageable response
        return Helper.getPageableResponse(page,ProductDTO.class);
    }
    /**
     * Retrieves a paginated and sorted list of live products.
     *
     * This method fetches all products that are marked as live (i.e., available for sale).
     * The results are paginated according to the specified page number and size,
     * and can be sorted by a specified field in either ascending or descending order.
     *
     * @param pageNumber the page number to retrieve (zero-based index)
     * @param pageSize   the number of products per page
     * @param sortBy     the field by which to sort the results
     * @param sortDir    the direction of sorting, either "asc" for ascending or "desc" for descending
     * @return a PageableResponse containing the paginated list of live ProductDTOs
     * @throws ResourceNotFoundException if no live products are found
     */
    @Override
    public PageableResponse<ProductDTO> getAllProductLive(int pageNumber, int pageSize, String sortBy, String sortDir) {
        // Fetch the products that are marked as live, with pagination and sorting
       Page<Product>page= productRepository.findByLive(true,PageRequest.of(pageNumber,pageSize,
                (sortDir.equalsIgnoreCase("desc"))?
                        (Sort.by(sortBy).descending())
                        :
                        (Sort.by(sortBy).ascending()))).orElseThrow(()->new ResourceNotFoundException("Product Not found"));
        // Return the pageable response containing live products
        return Helper.getPageableResponse(page,ProductDTO.class);
    }
    /**
     * Retrieves a paginated and sorted list of products that are not live.
     *
     * This method fetches all products that are marked as not live (i.e., unavailable for sale).
     * The results are paginated according to the specified page number and size,
     * and can be sorted by a specified field in either ascending or descending order.
     *
     * @param pageNumber the page number to retrieve (zero-based index)
     * @param pageSize   the number of products per page
     * @param sortBy     the field by which to sort the results
     * @param sortDir    the direction of sorting, either "asc" for ascending or "desc" for descending
     * @return a PageableResponse containing the paginated list of non-live ProductDTOs
     * @throws ResourceNotFoundException if no non-live products are found
     */
    @Override
    public PageableResponse<ProductDTO> getAllProductNotLive(int pageNumber, int pageSize, String sortBy, String sortDir) {
        // Fetch the products that are marked as not live, with pagination and sorting
        Page<Product>page= productRepository.findByLive(false,PageRequest.of(pageNumber,pageSize,
                (sortDir.equalsIgnoreCase("desc"))?
                        (Sort.by(sortBy).descending())
                        :
                        (Sort.by(sortBy).ascending()))).orElseThrow(()->new ResourceNotFoundException("Product Not found"));
        // Return the pageable response containing non-live products
        return Helper.getPageableResponse(page,ProductDTO.class);
    }
    /**
     * Retrieves a paginated and sorted list of products that were added on a specific date.
     *
     * This method fetches all products that match the specified date of addition.
     * The results are paginated based on the provided page number and size,
     * and can be sorted by a specified field in either ascending or descending order.
     *
     * @param date       the date on which the products were added
     * @param pageNumber the page number to retrieve (zero-based index)
     * @param pageSize   the number of products per page
     * @param sortBy     the field by which to sort the results
     * @param sortDir    the direction of sorting, either "asc" for ascending or "desc" for descending
     * @return a PageableResponse containing the paginated list of products added on the specified date
     * @throws ResourceNotFoundException if no products are found for the specified date
     */
    @Override
    public PageableResponse<ProductDTO> getAllProductByDate(Date date,int pageNumber, int pageSize, String sortBy, String sortDir) {
        // Fetch the products added on the specified date, with pagination and sorting
        Page<Product> page=productRepository.findByAddedDate(date,PageRequest.of(pageNumber,pageSize,
                (sortDir.equalsIgnoreCase("desc"))?
                        (Sort.by(sortBy).descending())
                        :
                        (Sort.by(sortBy).ascending()))).orElseThrow(()->new ResourceNotFoundException("Product Not found"));
        // Return the pageable response containing products added on the specified date
        return Helper.getPageableResponse(page,ProductDTO.class);
    }
    /**
     * Retrieves a paginated and sorted list of products that match a specified price.
     *
     * This method fetches all products that are priced at the specified amount.
     * The results are paginated based on the provided page number and size,
     * and can be sorted by a specified field in either ascending or descending order.
     *
     * @param price      the price to filter products by
     * @param pageNumber the page number to retrieve (zero-based index)
     * @param pageSize   the number of products per page
     * @param sortBy     the field by which to sort the results
     * @param sortDir    the direction of sorting, either "asc" for ascending or "desc" for descending
     * @return a PageableResponse containing the paginated list of products matching the specified price
     * @throws ResourceNotFoundException if no products are found at the specified price
     */
    @Override
    public PageableResponse<ProductDTO> getAllProductByPrice(double price,int pageNumber, int pageSize, String sortBy, String sortDir) {
        // Fetch the products matching the specified price, with pagination and sorting
      Page<Product>page=productRepository.findByPrice(price,PageRequest.of(pageNumber,pageSize,
                (sortDir.equalsIgnoreCase("desc"))?
                        (Sort.by(sortBy).descending())
                        :
                        (Sort.by(sortBy).ascending()))).orElseThrow(()->new ResourceNotFoundException("Product Not found"));
        // Return the pageable response containing products matching the specified price
        return Helper.getPageableResponse(page,ProductDTO.class);
    }
    /**
     * Retrieves a paginated and sorted list of products that fall within a specified price range.
     *
     * This method fetches all products whose prices are between the specified minimum and maximum values.
     * The results are paginated based on the provided page number and size,
     * and can be sorted by a specified field in either ascending or descending order.
     *
     * @param minPrice   the minimum price to filter products by
     * @param maxPrice   the maximum price to filter products by
     * @param pageNumber the page number to retrieve (zero-based index)
     * @param pageSize   the number of products per page
     * @param sortBy     the field by which to sort the results
     * @param sortDir    the direction of sorting, either "asc" for ascending or "desc" for descending
     * @return a PageableResponse containing the paginated list of products within the specified price range
     * @throws ResourceNotFoundException if no products are found within the specified price range
     */
    @Override
    public PageableResponse<ProductDTO> getAllProductBetween(double minPrice, double maxPrice,int pageNumber, int pageSize, String sortBy, String sortDir) {
        // Fetch the products matching the specified price range, with pagination and sorting
        Page<Product>page=productRepository.findByPriceBetween(minPrice,maxPrice,PageRequest.of(pageNumber,pageSize,
                (sortDir.equalsIgnoreCase("desc"))?
                        (Sort.by(sortBy).descending())
                        :
                        (Sort.by(sortBy).ascending()))).orElseThrow(()->new ResourceNotFoundException("Product Not found"));
        // Return the pageable response containing products within the specified price range
        return Helper.getPageableResponse(page,ProductDTO.class);
    }
    /**
     * Creates a new product with the specified details and assigns it to a category.
     * This method generates a unique product ID and sets the current date as the added date
     * for the new product.
     * It then saves the product to the repository and returns the corresponding ProductDTO.
     * @param productDTO the data transfer object containing the details of the product to be created
     * @return the created ProductDTO after saving it in the repository
     * @throws IllegalArgumentException if the productDTO is null or contains invalid data
     */
    @Override
    public ProductDTO createWithCategory(ProductDTO productDTO) {
        // Generate a unique ID for the product and set the added date
        productDTO.setProductId(UUID.randomUUID().toString());
        productDTO.setAddedDate(new Date());
        // Map the ProductDTO to Product entity and save it to the repository
        Product product=mapper.map(productDTO,Product.class);
        return mapper.map(productRepository.save(product),ProductDTO.class);
    }
    /**
     * Retrieves a paginated list of products that belong to a specified category.
     * This method fetches a category by its ID and then retrieves all products associated with
     * that category, applying pagination and sorting based on the provided parameters.
     * If the category or any products in that category are not found, it throws an appropriate exception.
     *
     * @param categoryId the ID of the category to filter products by
     * @param pageNumber the page number to retrieve (starting from 0)
     * @param pageSize the number of products per page
     * @param sortBy the field by which to sort the products
     * @param sortDir the direction of sorting, either "asc" for ascending or "desc" for descending
     * @return a PageableResponse containing the list of ProductDTOs that belong to the specified category
     * @throws ResourceNotFoundException if the category is not found or if no products exist in the category
     */
    @Override
    public PageableResponse<ProductDTO> getAllProductByCategory(String categoryId, int pageNumber, int pageSize, String sortBy, String sortDir) {
        // Fetch the category by ID; throw an exception if not found
        Category category=categoryRepository.findById(categoryId).orElseThrow(()->new ResourceNotFoundException("Category not found."));
        // Retrieve the paginated list of products associated with the category
        Page<Product> page=productRepository.findByCategories(category,PageRequest.of(pageNumber,pageSize,
                (sortDir.equalsIgnoreCase("desc"))?
                        (Sort.by(sortBy).descending())
                        :
                        (Sort.by(sortBy).ascending()))).orElseThrow(()->new ResourceNotFoundException("Product Not found"));
        // Return a pageable response containing the product data transfer objects
        return Helper.getPageableResponse(page,ProductDTO.class);
    }
    /**
     * Adds a category to a product by their respective IDs.
     * This method fetches a product and a category by their IDs and adds the specified
     * category to the product's list of categories. If either the product or category
     * is not found, an exception is thrown. The updated product is then saved to the
     * repository, and a ProductDTO is returned.
     *
     * @param productId the ID of the product to which the category will be added
     * @param categoryId the ID of the category to be added to the product
     * @return the updated ProductDTO after the category has been added
     * @throws ResourceNotFoundException if the product or category is not found
     */
    @Override
    public ProductDTO addCategory(String productId, String categoryId) {
        // Fetch the category by ID; throw an exception if not found
        Category category=categoryRepository.findById(categoryId)
                .orElseThrow(()->new ResourceNotFoundException("Category not found by category id."));
        // Fetch the product by ID; throw an exception if not found
        Product product=productRepository.findById(productId)
                .orElseThrow(()->new ResourceNotFoundException("Product Not found by product id"));
        // Add the category to the product's list of categories
        List<Category> categories=product.getCategories();
        categories.add(category);
        product.setCategories(categories);
        // Save the updated product and return the ProductDTO
        return mapper.map(productRepository.save(product),ProductDTO.class);
    }
    /**
     * Removes a category from a product by their respective IDs.
     * This method fetches a product and a category by their IDs, and removes the specified
     * category from the product's list of categories.
     * If either the product or category is not found, an exception is thrown.
     * The updated product is then saved to the repository, and a ProductDTO is returned.
     *
     * @param productId the ID of the product from which the category will be removed
     * @param categoryId the ID of the category to be removed from the product
     * @return the updated ProductDTO after the category has been removed
     * @throws ResourceNotFoundException if the product or category is not found
     */
    @Override
    public ProductDTO removeCategory(String productId,String categoryId) {
        // Fetch the category by ID; throw an exception if not found
        Category category=categoryRepository.findById(categoryId)
                .orElseThrow(()->new ResourceNotFoundException("Category not found by category id."));
        // Fetch the product by ID; throw an exception if not found
        Product product=productRepository.findById(productId)
                .orElseThrow(()->new ResourceNotFoundException("Product Not found by product id"));
        // Remove the category from the product's list of categories
        List<Category> categories=product.getCategories();
        categories.remove(category);
        product.setCategories(categories);
        // Save the updated product and return the ProductDTO
        return mapper.map(productRepository.save(product),ProductDTO.class);
    }
}
