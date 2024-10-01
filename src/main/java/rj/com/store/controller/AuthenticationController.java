package rj.com.store.controller;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import rj.com.store.datatransferobjects.JWTRequest;
import rj.com.store.datatransferobjects.JWTResponse;
import rj.com.store.datatransferobjects.UserDTO;
import rj.com.store.enities.User;
import rj.com.store.security.JwtHelper;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {
    private final Logger logger= LoggerFactory.getLogger(AuthenticationController.class);
    private AuthenticationManager authenticationManager;
    private JwtHelper jwtHelper;
    private UserDetailsService userDetailsService;
    ModelMapper modelMapper;
    public AuthenticationController(AuthenticationManager authenticationManager,JwtHelper jwtHelper,UserDetailsService userDetailsService,ModelMapper modelMapper){
        this.authenticationManager=authenticationManager;
        this.jwtHelper=jwtHelper;
        this.userDetailsService=userDetailsService;
        this.modelMapper=modelMapper;
    }
    @PostMapping("/generate-token")
    public ResponseEntity<JWTResponse> login(@RequestBody JWTRequest jwtRequest) {

        logger.info("Username {}, Password {}", jwtRequest.getEmail(), jwtRequest.getPassword());
        this.doAuthenticate(jwtRequest.getEmail(),jwtRequest.getPassword());
        User user = (User) this.userDetailsService.loadUserByUsername(jwtRequest.getEmail());
        String token = jwtHelper.generateToken(user);
        return ResponseEntity.ok(
                JWTResponse
                        .builder()
                        .token(token)
                        .user(modelMapper.map(user, UserDTO.class))
                        .build());
    }

    private void doAuthenticate(String email, String password) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
        }catch (BadCredentialsException e) {
            throw new BadCredentialsException("User not found of given username or password");
        }
    }
}
