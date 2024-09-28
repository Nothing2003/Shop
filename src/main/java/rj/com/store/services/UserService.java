package rj.com.store.services;

import rj.com.store.datatransferobjects.UserDTO;
import rj.com.store.datatransferobjects.PageableResponse;

import java.io.IOException;

public interface UserService {
    //create
    UserDTO createUser(UserDTO userDTO);
    //update
    UserDTO UpdateUser(UserDTO userDTO, String userId);
    //delete
    void deleteUser(String userId) throws IOException;
    //get all user
    PageableResponse<UserDTO> getAllUser(int pageNumber, int pageSize, String sortBy, String sortDir);
    //get user by id
    UserDTO getUserById(String userId);
    // get user by email
    UserDTO getUserByEmail(String userEmail);
    // search user 
    PageableResponse<UserDTO> searchUser(String keyword,int pageNumber,int pageSize,String sortBy,String sortDir);
}
