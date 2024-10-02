package rj.com.store.services.servicesimp;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
import java.util.UUID;

@Service
public class ProductServiceImp implements ProductService {
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ModelMapper mapper;
    @Autowired
    private  ImageServiceInCloud imageServiceInCloud;
    @Autowired
    private CategoryRepository categoryRepository;
    private final Logger logger= LoggerFactory.getLogger(ProductServiceImp.class);
    @Override
    public ProductDTO createProduct(ProductDTO productDTO) {
       productDTO.setProductId(UUID.randomUUID().toString());
       productDTO.setAddedDate(new Date());
       logger.info("Product Add : {}",1);
       return mapper.map(
                productRepository.save(
                        mapper.map(productDTO,Product.class)
                )
               ,ProductDTO.class
       );
    }
    @Override
    public void DeleteProduct(String productId) {
       Product product= productRepository.findById(productId).orElseThrow(()->new ResourceNotFoundException("Product is not found"));
       imageServiceInCloud.deleteImage(product.getProductImageName());
       productRepository.delete(product);
       logger.info("Product Delete : {}",1);
    }
    @Override
    public ProductDTO updateProduct(ProductDTO productDTO, String productId) {
      Product product=productRepository.findById(productId).orElseThrow(()->new ResourceNotFoundException("Product Not Found"));
      product.setTitle(productDTO.getTitle());
      product.setLive(productDTO.isLive());
      product.setDescription(productDTO.getDescription());
      product.setQuantity(productDTO.getQuantity());
      product.setPrice(productDTO.getPrice());
      product.setStock(productDTO.isStock());
      product.setDiscountedPrice(productDTO.getDiscountedPrice());
      if (product.getProductImageName().equalsIgnoreCase(productDTO.getProductId())){
          product.setProductImageName(productDTO.getProductImageName());
      }else{
          imageServiceInCloud.deleteImage(product.getProductImageName());
          product.setProductImageName(productDTO.getProductImageName());
      }
      logger.info("Product Update : {}",1);
      return mapper.map(productRepository.save(product),ProductDTO.class);
    }
    @Override
    public ProductDTO getSingleProduct(String productId) {
        Product product=productRepository.findById(productId).orElseThrow(()->new ResourceNotFoundException("Product not found"));
        return mapper.map(product,ProductDTO.class);
    }
    @Override
    public PageableResponse<ProductDTO> getAllProduct(int pageNumber, int pageSize, String sortBy, String sortDir) {
      Page<Product> page= productRepository.findAll(PageRequest.of(pageNumber,pageSize,
                (sortDir.equalsIgnoreCase("desc"))?
                        (Sort.by(sortBy).descending())
                        :
                        (Sort.by(sortBy).ascending())));
        return Helper.getPageableResponse(page,ProductDTO.class);
    }
    @Override
    public PageableResponse<ProductDTO> searchProduct(String title,int pageNumber, int pageSize, String sortBy, String sortDir) {
     Page<Product>page= productRepository.findByTitleContaining(title,PageRequest.of(pageNumber,pageSize,
                (sortDir.equalsIgnoreCase("desc"))?
                        (Sort.by(sortBy).descending())
                        :
                        (Sort.by(sortBy).ascending()))).orElseThrow(()->new ResourceNotFoundException("Products not found exception"));
        return Helper.getPageableResponse(page,ProductDTO.class);
    }
    @Override
    public PageableResponse<ProductDTO> getAllProductLive(int pageNumber, int pageSize, String sortBy, String sortDir) {
       Page<Product>page= productRepository.findByLive(true,PageRequest.of(pageNumber,pageSize,
                (sortDir.equalsIgnoreCase("desc"))?
                        (Sort.by(sortBy).descending())
                        :
                        (Sort.by(sortBy).ascending()))).orElseThrow(()->new ResourceNotFoundException("Product Not found"));
        return Helper.getPageableResponse(page,ProductDTO.class);
    }
    @Override
    public PageableResponse<ProductDTO> getAllProductNotLive(int pageNumber, int pageSize, String sortBy, String sortDir) {
        Page<Product>page= productRepository.findByLive(false,PageRequest.of(pageNumber,pageSize,
                (sortDir.equalsIgnoreCase("desc"))?
                        (Sort.by(sortBy).descending())
                        :
                        (Sort.by(sortBy).ascending()))).orElseThrow(()->new ResourceNotFoundException("Product Not found"));
        return Helper.getPageableResponse(page,ProductDTO.class);
    }
    @Override
    public PageableResponse<ProductDTO> getAllProductByDate(Date date,int pageNumber, int pageSize, String sortBy, String sortDir) {
        Page<Product> page=productRepository.findByAddedDate(date,PageRequest.of(pageNumber,pageSize,
                (sortDir.equalsIgnoreCase("desc"))?
                        (Sort.by(sortBy).descending())
                        :
                        (Sort.by(sortBy).ascending()))).orElseThrow(()->new ResourceNotFoundException("Product Not found"));
        return Helper.getPageableResponse(page,ProductDTO.class);
    }
    @Override
    public PageableResponse<ProductDTO> getAllProductByPrice(double price,int pageNumber, int pageSize, String sortBy, String sortDir) {
      Page<Product>page=productRepository.findByPrice(price,PageRequest.of(pageNumber,pageSize,
                (sortDir.equalsIgnoreCase("desc"))?
                        (Sort.by(sortBy).descending())
                        :
                        (Sort.by(sortBy).ascending()))).orElseThrow(()->new ResourceNotFoundException("Product Not found"));
        return Helper.getPageableResponse(page,ProductDTO.class);
    }
    @Override
    public PageableResponse<ProductDTO> getAllProductBetween(double minPrice, double maxPrice,int pageNumber, int pageSize, String sortBy, String sortDir) {
        Page<Product>page=productRepository.findByPriceBetween(minPrice,maxPrice,PageRequest.of(pageNumber,pageSize,
                (sortDir.equalsIgnoreCase("desc"))?
                        (Sort.by(sortBy).descending())
                        :
                        (Sort.by(sortBy).ascending()))).orElseThrow(()->new ResourceNotFoundException("Product Not found"));
        return Helper.getPageableResponse(page,ProductDTO.class);
    }

    @Override
    public ProductDTO createWithCategory(ProductDTO productDTO, String categoryId) {
        Category category=categoryRepository.findById(categoryId).orElseThrow(()->new ResourceNotFoundException("Category not found."));
        productDTO.setProductId(UUID.randomUUID().toString());
        productDTO.setAddedDate(new Date());
        Product product=mapper.map(productDTO,Product.class);
        product.setCategory(mapper.map(category, Category.class));
        logger.info("Product Add : {}",1);
        return mapper.map(productRepository.save(product),ProductDTO.class);

    }

    @Override
    public PageableResponse<ProductDTO> getAllProductByCategory(String categoryId, int pageNumber, int pageSize, String sortBy, String sortDir) {
        Category category=categoryRepository.findById(categoryId).orElseThrow(()->new ResourceNotFoundException("Category not found."));
        Page<Product> page=productRepository.findByCategory(category,PageRequest.of(pageNumber,pageSize,
                (sortDir.equalsIgnoreCase("desc"))?
                        (Sort.by(sortBy).descending())
                        :
                        (Sort.by(sortBy).ascending()))).orElseThrow(()->new ResourceNotFoundException("Product Not found"));
        return Helper.getPageableResponse(page,ProductDTO.class);
    }

    @Override
    public ProductDTO updateCategory(String productId, String categoryId) {
        Category category=categoryRepository.findById(categoryId).orElseThrow(()->new ResourceNotFoundException("Category not found by category id."));
        Product product=productRepository.findById(productId).orElseThrow(()->new ResourceNotFoundException("Product Not found by product id"));
        product.setCategory(category);
        return mapper.map(productRepository.save(product),ProductDTO.class);
    }
}
