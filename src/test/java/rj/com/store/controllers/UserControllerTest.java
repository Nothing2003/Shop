package rj.com.store.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.modelmapper.internal.bytebuddy.implementation.bytecode.Throw;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import rj.com.store.controller.UserController;
import rj.com.store.datatransferobjects.UserDTO;
import rj.com.store.enities.Role;
import rj.com.store.enities.User;
import rj.com.store.services.UserService;

import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {
    @MockBean
    private UserService userService;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private MockMvc mockMvc;
    Role role;
    User user;
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
    }
    @Test
    public void createUser() throws Exception {
        UserDTO userDTO=modelMapper.map(user,UserDTO.class);
        Mockito.when(userService.createUser(Mockito.any())).thenReturn(userDTO);
        mockMvc.perform(
                MockMvcRequestBuilders.post("/users/v1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(convertObjectToString(userDTO))
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").exists()
                );

    }

    private String convertObjectToString(Object object) {
        try {
            return new ObjectMapper().writeValueAsString(object);
        }catch (Exception e) {

            return "";
        }
    }
}
