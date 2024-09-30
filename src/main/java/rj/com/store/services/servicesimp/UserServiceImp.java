package rj.com.store.services.servicesimp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import rj.com.store.datatransferobjects.PageableResponse;
import rj.com.store.datatransferobjects.UserDTO;
import rj.com.store.enities.Role;
import rj.com.store.enities.User;
import rj.com.store.exceptions.ResourceNotFoundException;
import rj.com.store.helper.Helper;
import rj.com.store.repositories.RoleRepository;
import rj.com.store.repositories.UserRepositories;
import rj.com.store.services.ImageServiceInCloud;
import rj.com.store.services.UserService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Service
public class UserServiceImp implements UserService {
    private final Logger logger= LoggerFactory.getLogger(UserService.class);
    @Value("${user.profile.image.path}")
    private String imagePath;
    @Autowired
    private UserRepositories userRepositories;
    @Autowired
    private ImageServiceInCloud imageServiceInCloud;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    RoleRepository roleRepository;
    @Override
    public UserDTO createUser(UserDTO userDTO) {
        //Generate ID
        String id=UUID.randomUUID().toString();
        userDTO.setUserId(id);
        userDTO.setPassword(passwordEncoder.encode(userDTO.getPassword()));

        //that method convert dto -> entity
        User user = userdtoToEntity(userDTO);
        //By default, all user are NORMAL
        Role role1 =roleRepository.findByRoleName("ROLE_NORMAL").orElse(null);
        if (role1 == null) {
            role1 = new Role();
            role1.setRoleId(UUID.randomUUID().toString());
            role1.setRoleName("ROLE_NORMAL");
            roleRepository.save(role1);
        }
        user.setRoles(List.of(role1));
        User saveUser = userRepositories.save(user);
        //that method convert  entity ->  dto
        return entityToUserDTO(saveUser);
    }

    @Override
    public UserDTO UpdateUser(UserDTO userDTO, String userId) {
      User user= userRepositories
              .findById(userId)
              .orElseThrow(()->new ResourceNotFoundException("User is not found"));
      user.setName(userDTO.getName());
      user.setAbout(userDTO.getAbout());

      if (user.getImageName().equalsIgnoreCase(userDTO.getImageName())){
          user.setImageName(userDTO.getImageName());
      }
      else {
          imageServiceInCloud.deleteImage(user.getImageName());
          user.setImageName(userDTO.getImageName());
      }

      user.setGender(userDTO.getGender());
      if (userDTO.getPassword().equals(user.getPassword())){
          user.setPassword(userDTO.getPassword());
      }
      else{
          user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
      }

      User updatedUser= userRepositories.save(user);
      return entityToUserDTO(updatedUser);
    }

    @Override
    public void deleteUser(String userId){
        User user=userRepositories
                .findById(userId)
                .orElseThrow(()->new ResourceNotFoundException("User not found exception"));
            String fullPath=imagePath+user.getImageName();
//        try {
//            Files.delete(Paths.get(fullPath));
//        } catch (IOException e){
//            logger.info("Exception is {}",e.getMessage());
//
//        }
        imageServiceInCloud.deleteImage(user.getImageName());
        userRepositories.delete(user);
    }

    @Override
    public PageableResponse<UserDTO> getAllUser(int pageNumber,int pageSize,String sortBy,String sortDir) {
        Page<User> page=userRepositories.findAll(
                PageRequest
                .of(
                        pageNumber,
                        pageSize,
                        (
                                sortDir
                                        .equalsIgnoreCase("desc"))?
                                (
                                        Sort
                                                .by(sortBy)
                                                .descending()
                                )
                                :
                                (
                                        Sort
                                                .by(sortBy)
                                                .ascending()
                                )
                )
        );

        return Helper.getPageableResponse(page,UserDTO.class);
    }

    @Override
    public UserDTO getUserById(String userId) {
      User user= userRepositories
              .findById(userId)
              .orElseThrow(()->new ResourceNotFoundException("User Not found with given User Id"));
      return entityToUserDTO(user);
    }

    @Override
    public UserDTO getUserByEmail(String userEmail) {
       User user= userRepositories
               .findByEmail(userEmail)
               .orElseThrow(()->new ResourceNotFoundException("User not found with given user email"));
        return  entityToUserDTO(user);
    }

    @Override
    public PageableResponse<UserDTO> searchUser(String keyword,int pageNumber,int pageSize,String sortBy,String sortDir) {
     Page<User> page =userRepositories.findByNameContaining(keyword,PageRequest
             .of(
                     pageNumber,
                     pageSize,
                     (
                             sortDir
                                     .equalsIgnoreCase("desc"))?
                             (
                                     Sort
                                             .by(sortBy)
                                             .descending()
                             )
                             :
                             (
                                     Sort
                                             .by(sortBy)
                                             .ascending()
                             )
             )
     )
             .orElseThrow(()->new ResourceNotFoundException("User not found with given user email"));

       return Helper.getPageableResponse(page,UserDTO.class);
    }
    //That method convert  entity ->dto
    private UserDTO entityToUserDTO(User saveUser) {
        UserDTO userDTO=new UserDTO();
        BeanUtils.copyProperties(saveUser,userDTO);
        return userDTO;
    }
    //That method convert dto -> entity
    private User userdtoToEntity(UserDTO userDTO) {
        User user =new User();
        BeanUtils.copyProperties(userDTO,user);
        return user;
    }
}
