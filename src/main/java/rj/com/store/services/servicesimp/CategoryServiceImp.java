package rj.com.store.services.servicesimp;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import rj.com.store.datatransferobjects.CategoryDTO;
import rj.com.store.datatransferobjects.PageableResponse;
import rj.com.store.enities.Category;
import rj.com.store.exceptions.ResourceNotFoundException;
import rj.com.store.helper.Helper;
import rj.com.store.repositories.CategoryRepository;
import rj.com.store.services.CategoryService;
import rj.com.store.services.ImageServiceInCloud;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class CategoryServiceImp implements CategoryService {
    Logger logger= LoggerFactory.getLogger(CategoryServiceImp.class);
    @Autowired
    private final CategoryRepository categoryRepository;
    @Autowired
    private  final ModelMapper mapper;
    @Autowired
    private final ImageServiceInCloud imageServiceInCloud;
    public CategoryServiceImp(CategoryRepository categoryRepository, ModelMapper mapper, ImageServiceInCloud imageServiceInCloud){
        this.imageServiceInCloud=imageServiceInCloud;
        this.mapper=mapper;
        this.categoryRepository = categoryRepository;

    }
    @Value("${category.cover.image.path}")
    String imageUploadPath;
    @Override
    public CategoryDTO createCategory(CategoryDTO categoryDTO) {
        categoryDTO.setCategoryId(UUID.randomUUID().toString());
        Category category=mapper.map(categoryDTO,Category.class);

       return mapper.map(
               categoryRepository
                       .save(category)
               ,CategoryDTO.class
       );
    }

    @Override
    public CategoryDTO updateCategory(CategoryDTO categoryDTO,String categoryId) {
      Category category= categoryRepository.findById(categoryId).orElseThrow(()->new ResourceNotFoundException("Category Not found exception."));
      category.setTitle(categoryDTO.getTitle());
      if (category.getCoverImage().equalsIgnoreCase(categoryDTO.getCoverImage())){
          category.setCoverImage(categoryDTO.getCoverImage());
      }
      else {
          imageServiceInCloud.deleteImage(category.getCoverImage());
          category.setCoverImage(categoryDTO.getCoverImage());
      }
      category.setCoverImage(categoryDTO.getCoverImage());
      category.setDescription(categoryDTO.getDescription());
      return mapper.map(categoryRepository.save(category),CategoryDTO.class);

    }

    @Override
    public void deleteCategory(String categoryId) {
        Category category= categoryRepository.findById(categoryId).orElseThrow(()->new ResourceNotFoundException("Category Not found exception."));
        imageServiceInCloud.deleteImage(category.getCoverImage());
        categoryRepository.delete(category);
    }

    @Override
    public PageableResponse<CategoryDTO> getAllCategory(int pageNumber, int pageSize, String sortBy, String sortDir) {
       Page<Category> page= categoryRepository.findAll(
                PageRequest
                        .of(pageNumber, pageSize,
                                (sortDir.equalsIgnoreCase("desc"))?
                                        (Sort.by(sortBy).descending())
                                        :
                                        (Sort.by(sortBy).ascending())
                        )
        );
        return Helper.getPageableResponse(page,CategoryDTO.class);
    }
    @Override
    public CategoryDTO getCategoryById(String id) {
        Category category= categoryRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Category Not found exception."));

        String fullPath=imageUploadPath+category.getCoverImage();
        try {

            Files.delete(Paths.get(fullPath));
        } catch (IOException e){
            logger.info("Exception is {}",e.getMessage());

        }
        return  mapper.map(category,CategoryDTO.class);
    }

    @Override
    public PageableResponse<CategoryDTO> searchCategory(String keyword, int pageNumber, int pageSize, String sortBy, String sortDir) {
      Page<Category>page=  categoryRepository.findByTitleContaining(keyword,
                PageRequest.of(pageNumber, pageSize,
                                (sortDir.equalsIgnoreCase("desc"))?
                                        (Sort.by(sortBy).descending())
                                        :
                                        (Sort.by(sortBy).ascending())
                        )
                ).orElseThrow(()->new ResourceNotFoundException("Category not found"));

        return Helper.getPageableResponse(page,CategoryDTO.class);
    }
}
