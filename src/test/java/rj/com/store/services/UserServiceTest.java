package rj.com.store.services;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import rj.com.store.datatransferobjects.PageableResponse;
import rj.com.store.datatransferobjects.UserDTO;
import rj.com.store.enities.Role;
import rj.com.store.enities.User;
import rj.com.store.helper.AppCon;
import rj.com.store.repositories.RoleRepository;
import rj.com.store.repositories.UserRepositories;
import java.io.IOException;
import java.util.*;
@SpringBootTest
public class UserServiceTest {
    @MockBean
    private UserRepositories userRepositories;
    @MockBean
    private RoleRepository roleRepository;
    @Autowired
    ModelMapper modelMapper;
    @Autowired
    private UserService userService;
    User user;
    Role role;
    String rollId;
    @BeforeEach
    public void setUp() {
        role = new Role();
        role.setRoleId("abc");
        role.setRoleName("NORMAL");

     user=   User.builder()
                .name("Soumojit Makar")
                .email("Soumojit@gmail.com")
                .password("soumojit123")
                .about("This is a test")
                .gender("Male")
                .imageName("abc.png")
                .roles(List.of(role))
                .build();
     rollId="abc";
    }
    @Test
    public void createUser() {
        Mockito.when(userRepositories.save(Mockito.any())).thenReturn(user);
        Mockito.when(roleRepository.findById(Mockito.any())).thenReturn(Optional.of(role));
        UserDTO userDTO= userService.createUser(modelMapper.map(user, UserDTO.class));
        String name="Soumojit Makar";
        Assertions.assertEquals(name,userDTO.getName(),"Create User Failed");
        Assertions.assertNotNull(userDTO);
    }
    @Test
    public void updateUser() {
        String userId="hdbwb";
        UserDTO userDTO=UserDTO.builder()
                .name("Soumojit Makar 1")
                .email("Soumojit@gmail.com")
                .about("This is updated")
                .password("soumojit123")
                .gender("Male")
                .imageName("abc.png")
                .build();
             Mockito.when(userRepositories.findById(Mockito.anyString())).thenReturn(Optional.of(user));
             Mockito.when(userRepositories.save(Mockito.any())).thenReturn(user);
             UserDTO updatedUser= userService.UpdateUser(userDTO,userId);
             Assertions.assertNotNull(updatedUser);
             Assertions.assertEquals(updatedUser.getName(),userDTO.getName(),"Update failed");
    }
    @Test
    public void deleteUserTest() throws IOException {
        String userId="hdbwb";
        Mockito.when(userRepositories.findById("hdbwb")).thenReturn(Optional.of(user));
        userService.deleteUser(userId);
        Mockito.verify(userRepositories, Mockito.times(1)).delete(user);
    }
    @Test
    public void getAllUsersTest() {
      User  user2=   User.builder()
                .name("Soumojit Makar2")
                .email("Soumojit@gmail.com")
                .password("soumojit123")
                .about("This is a test")
                .gender("Male")
                .imageName("abc.png")
                .roles(List.of(role))
                .build();
      User  user3=   User.builder()
                .name("Soumojit Makar")
                .email("Soumojit@gmail.com")
                .password("soumojit123")
                .about("This is a test")
                .gender("Male")
                .imageName("abc.png")
                .roles(List.of(role))
                .build();
      List<User> users= Arrays.asList(user,user2,user3);
      Page<User>page=new PageImpl<User>(users);
      Mockito.when(userRepositories.findAll((Pageable) Mockito.any())).thenReturn(page);
      PageableResponse<UserDTO> all= userService.getAllUser(1, 2,"name", AppCon.Sort_Dir);
      Assertions.assertEquals(3,all.getContent().size(),"Get All User is Failed");
    }
    @Test
    public void getUserByIdTest() {
        String userId="hdbwb";
        Mockito.when(userRepositories.findById(userId)).thenReturn(Optional.of(user));
        UserDTO userDTO= userService.getUserById(userId);
        Assertions.assertNotNull(userDTO);
        Assertions.assertEquals(userDTO.getName(),user.getName(),"Get User By Id is Failed");
    }
    @Test
    public void getUserByEmailTest() {
        String userEmail="Soumojit@gmail.com";
        Mockito.when(userRepositories.findByEmail(userEmail)).thenReturn(Optional.of(user));
        UserDTO userDTO= userService.getUserByEmail(userEmail);
        Assertions.assertNotNull(userDTO);
        Assertions.assertEquals(userDTO.getEmail(),user.getEmail(),"Get User By Email is Failed");
    }
    @Test
    public void searchUserTest() {
//        User  user2=   User.builder()
//                .name("Ayan")
//                .email("Soumojit@gmail.com")
//                .password("soumojit123")
//                .about("This is a test")
//                .gender("Male")
//                .imageName("abc.png")
//                .roles(List.of(role))
//                .build();
//        User  user3=   User.builder()
//                .name("Suvosree")
//                .email("Soumojit@gmail.com")
//                .password("soumojit123")
//                .about("This is a test")
//                .gender("Male")
//                .imageName("abc.png")
//                .roles(List.of(role))
//                .build();
//        List<User> users= Arrays.asList(user,user2,user3);
//        String keyword="Soumojit";
//        Page<User>page=new PageImpl<User>(users);
//
//        Mockito.when(userRepositories.findByNameContaining(keyword,)).thenReturn(Optional.of(page));
//        PageableResponse<UserDTO> all= userService.searchUser(keyword,1, 2,"name", AppCon.Sort_Dir);
////        Assertions.assertEquals(2,all.getContent().size(),"Search User is Failed");
    }
}
