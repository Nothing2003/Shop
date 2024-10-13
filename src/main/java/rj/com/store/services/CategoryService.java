package rj.com.store.services;

import rj.com.store.datatransferobjects.CategoryDTO;
import rj.com.store.datatransferobjects.PageableResponse;

/**
 * Interface for managing categories in the application.
 * This service provides methods for creating, updating, deleting,
 * retrieving, and searching categories.
 */
public interface CategoryService {

    /**
     * Creates a new category.
     *
     * @param categoryDTO The data transfer object containing category details.
     * @return The created CategoryDTO object.
     */
    CategoryDTO createCategory(CategoryDTO categoryDTO);

    /**
     * Updates an existing category by its ID.
     *
     * @param categoryDTO The data transfer object containing updated category details.
     * @param categoryId The ID of the category to update.
     * @return The updated CategoryDTO object.
     */
    CategoryDTO updateCategory(CategoryDTO categoryDTO, String categoryId);

    /**
     * Deletes a category by its ID.
     *
     * @param categoryId The ID of the category to delete.
     */
    void deleteCategory(String categoryId);

    /**
     * Retrieves a pageable list of all categories.
     *
     * @param pageNumber The page number to retrieve (0-based).
     * @param pageSize The number of items per page.
     * @param sortBy The field to sort the results by.
     * @param sortDir The direction to sort ("asc" for ascending, "desc" for descending).
     * @return A pageable response containing a list of CategoryDTO objects.
     */
    PageableResponse<CategoryDTO> getAllCategory(int pageNumber, int pageSize, String sortBy, String sortDir);

    /**
     * Retrieves the details of a single category by its ID.
     *
     * @param id The ID of the category to retrieve.
     * @return The CategoryDTO object for the requested category.
     */
    CategoryDTO getCategoryById(String id);

    /**
     * Searches for categories by a keyword and returns a pageable list of results.
     *
     * @param keyword The keyword to search for in category names or descriptions.
     * @param pageNumber The page number to retrieve (0-based).
     * @param pageSize The number of items per page.
     * @param sortBy The field to sort the results by.
     * @param sortDir The direction to sort ("asc" for ascending, "desc" for descending).
     * @return A pageable response containing a list of CategoryDTO objects matching the search criteria.
     */
    PageableResponse<CategoryDTO> searchCategory(String keyword, int pageNumber, int pageSize, String sortBy, String sortDir);
}
