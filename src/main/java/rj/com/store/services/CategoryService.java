package rj.com.store.services;

import rj.com.store.datatransferobjects.CategoryDTO;
import rj.com.store.datatransferobjects.PageableResponse;

public interface CategoryService {
    //create
    CategoryDTO createCategory(CategoryDTO categoryDTO);
    //update
    CategoryDTO updateCategory(CategoryDTO categoryDTO,String categoryId);
    //delete
    void deleteCategory(String categoryId);
    //get all
    PageableResponse<CategoryDTO> getAllCategory(int pageNumber, int pageSize, String sortBy, String sortDir);
//   get single category detail
    CategoryDTO getCategoryById(String id);
    //search
    PageableResponse<CategoryDTO> searchCategory(String keyword,int pageNumber, int pageSize, String sortBy, String sortDir);

}
