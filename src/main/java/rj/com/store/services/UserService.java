package rj.com.store.services;

import rj.com.store.datatransferobjects.UserDTO;
import rj.com.store.datatransferobjects.PageableResponse;

import java.io.IOException;

/**
 * Interface for managing user-related operations.
 * This service provides methods for creating, updating, deleting,
 * retrieving, and searching users in the system.
 */
public interface UserService {

    /**
     * Creates a new user.
     *
     * @param userDTO The data transfer object containing user details.
     * @return The created UserDTO object.
     */
    UserDTO createUser(UserDTO userDTO);

    /**
     * Updates an existing user by their ID.
     *
     * @param userDTO The data transfer object containing updated user details.
     * @param userId The ID of the user to update.
     * @return The updated UserDTO object.
     */
    UserDTO UpdateUser(UserDTO userDTO, String userId);

    /**
     * Deletes a user by their ID.
     *
     * @param userId The ID of the user to delete.
     * @throws IOException If an I/O error occurs during deletion.
     */
    void deleteUser(String userId) throws IOException;

    /**
     * Retrieves a pageable list of all users.
     *
     * @param pageNumber The page number to retrieve (0-based).
     * @param pageSize The number of items per page.
     * @param sortBy The field to sort the results by.
     * @param sortDir The direction to sort ("asc" for ascending, "desc" for descending).
     * @return A pageable response containing a list of UserDTO objects.
     */
    PageableResponse<UserDTO> getAllUser(int pageNumber, int pageSize, String sortBy, String sortDir);

    /**
     * Retrieves the details of a single user by their ID.
     *
     * @param userId The ID of the user to retrieve.
     * @return The UserDTO object for the requested user.
     */
    UserDTO getUserById(String userId);

    /**
     * Retrieves the details of a user by their email.
     *
     * @param userEmail The email of the user to retrieve.
     * @return The UserDTO object for the requested user.
     */
    UserDTO getUserByEmail(String userEmail);

    /**
     * Searches for users by a keyword and returns a pageable list of results.
     *
     * @param keyword The keyword to search for in user names or other details.
     * @param pageNumber The page number to retrieve (0-based).
     * @param pageSize The number of items per page.
     * @param sortBy The field to sort the results by.
     * @param sortDir The direction to sort ("asc" for ascending, "desc" for descending).
     * @return A pageable response containing a list of UserDTO objects matching the search criteria.
     */
    PageableResponse<UserDTO> searchUser(String keyword, int pageNumber, int pageSize, String sortBy, String sortDir);
}
