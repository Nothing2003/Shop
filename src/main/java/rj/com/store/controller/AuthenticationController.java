package rj.com.store.controller;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.apache.v2.ApacheHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.TableGenerator;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;
import rj.com.store.datatransferobjects.*;
import rj.com.store.enities.Providers;
import rj.com.store.enities.User;
import rj.com.store.exceptions.BadApiRequest;
import rj.com.store.exceptions.ResourceNotFoundException;
import rj.com.store.security.JwtHelper;
import rj.com.store.services.RefreshTokenService;
import rj.com.store.services.UserService;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;

@RestController
@RequestMapping("/auth/v1")
@SecurityRequirement(name = "scheme")
@Tag(name = "Authentication" ,description = "APIs for authentication ")
public class AuthenticationController {
    @Value("${app.google.client_id}")
    private String googleClientId;
    @Value("${app.google.password}")
    private String googlePassword;
    private final Logger logger= LoggerFactory.getLogger(AuthenticationController.class);
    private final AuthenticationManager authenticationManager;
    private final JwtHelper jwtHelper;
    private final UserDetailsService userDetailsService;
    private final ModelMapper modelMapper;
    private final UserService userService;
    private final RefreshTokenService refreshTokenService;

    public AuthenticationController(AuthenticationManager authenticationManager,JwtHelper jwtHelper,
                                    UserDetailsService userDetailsService,ModelMapper modelMapper,
                                    UserService userService, RefreshTokenService refreshTokenService){
        this.authenticationManager=authenticationManager;
        this.jwtHelper=jwtHelper;
        this.userDetailsService=userDetailsService;
        this.modelMapper=modelMapper;
        this.userService=userService;
        this.refreshTokenService=refreshTokenService;
    }
    @PostMapping("/generate-token")
    @Operation(summary = "Generate Token by JWTRequest")
    public ResponseEntity<JWTResponse> login(@RequestBody JWTRequest jwtRequest) {

        logger.info("Username {}, Password {}", jwtRequest.getEmail(), jwtRequest.getPassword());
        this.doAuthenticate(jwtRequest.getEmail(),jwtRequest.getPassword());
        User user = (User) this.userDetailsService.loadUserByUsername(jwtRequest.getEmail());
        String token = jwtHelper.generateToken(user);
        RefreshTokenDTO refreshTokenDTO= refreshTokenService.createRefreshToken(user.getEmail());
        return ResponseEntity.ok(
                JWTResponse
                        .builder()
                        .refreshToken(refreshTokenDTO)
                        .token(token)
                        .user(modelMapper.map(user, UserDTO.class))
                        .build());
    }
    @PostMapping("/regenerate-token")
    @Operation(summary = "Generate Token by Refresh Token Request")
    public ResponseEntity<JWTResponse>regenerateToken(@RequestBody RefreshTokenRequest refreshTokenRequest) {
        RefreshTokenDTO refreshTokenDTO=refreshTokenService.findByToken(refreshTokenRequest.getRefreshToken());
        RefreshTokenDTO refreshTokenDTO1=refreshTokenService.verifyRefreshToken(refreshTokenDTO);
        UserDTO userDTO= refreshTokenService.getUserByToken(refreshTokenDTO1);
        String jwtToken= jwtHelper.generateToken(modelMapper.map(userDTO,User.class));
        return ResponseEntity.ok(JWTResponse.builder().token(jwtToken)
                .user(userDTO)
                .refreshToken(refreshTokenDTO)
                .build());
    }

    //handle login with Google
    @PostMapping("/login-with-google")
    @Operation(summary = "Generate Token by Google Login Request")
    public ResponseEntity<JWTResponse> handleGoogleLogin(@RequestBody GoogleLoginRequest googleLoginRequest) throws GeneralSecurityException, IOException {
        logger.info("Google login request {}", googleLoginRequest.getIdToken());
        GoogleIdTokenVerifier token= new GoogleIdTokenVerifier.Builder(new ApacheHttpTransport(),new GsonFactory()).setAudience(List.of(googleClientId)).build();
        GoogleIdToken idToken=token.verify(googleLoginRequest.getIdToken());

        if(idToken!=null) {
            GoogleIdToken.Payload payload = idToken.getPayload();
            String email = payload.getEmail();
            String name = (String) payload.get("name");
            String pictureUrl = (String) payload.get("picture");
            UserDTO userDTO=UserDTO.builder()
                    .name(name)
                    .email(email)
                    .imageName(pictureUrl)
                    .password(googlePassword)
                    .about("User created by google")
                    .provider(Providers.GOOGLE)
                    .build();
            UserDTO user=null;
            try {
                 user= userService.getUserByEmail(email);
            }catch (ResourceNotFoundException ex){
                user= userService.createUser(userDTO);
            }
            logger.info("Email {}, Password {}",user.getEmail(),user.getPassword());
//            this.doAuthenticate(user.getEmail(),user.getPassword());
            User userAuth=(User) this.userDetailsService.loadUserByUsername(user.getEmail());
            String authToken = jwtHelper.generateToken(userAuth);
            RefreshTokenDTO refreshTokenDTO= refreshTokenService.createRefreshToken(userAuth.getEmail());
            return ResponseEntity.ok(
                    JWTResponse
                            .builder()
                            .refreshToken(refreshTokenDTO)
                            .token(authToken)
                            .user(user)
                            .build());
        }else {
            logger.error("Google login failed");
            throw new BadApiRequest("Google login failed");
        }
    }
    private void doAuthenticate(String email, String password) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
        }catch (BadCredentialsException e) {
            throw new BadCredentialsException("User not found of given username or password");
        }
    }
}