package rj.com.store.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import rj.com.store.datatransferobjects.ApiResponseMessage;
import rj.com.store.datatransferobjects.ImageResponse;
import rj.com.store.datatransferobjects.PageableResponse;
import rj.com.store.datatransferobjects.UserDTO;
import rj.com.store.enities.Providers;
import rj.com.store.exceptions.ResourceNotFoundException;
import rj.com.store.helper.AppCon;
import rj.com.store.services.FileService;
import rj.com.store.services.UserService;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

@RestController
@RequestMapping("/users")
@SecurityRequirement(name = "scheme")
@Tag(name = "Users Controller ",description = "This is user Api for users operation")
public class UserController {
    private Logger logger= LoggerFactory.getLogger(UserController.class);
    private final UserService userService;
    private final FileService fileService;
    @Value("${user.profile.image.path}")
    private String imageUploadPath;
    public UserController(UserService userService,FileService fileService) {
        this.userService = userService;
        this.fileService=fileService;
    }
    //create
    @PostMapping
    @Operation(summary = "create new user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",description = "Success | OK "),
            @ApiResponse(responseCode = "401",description = "not authorized !! "),
            @ApiResponse(responseCode = "201",description = "new User created !! ")
    }
    )
    public ResponseEntity<UserDTO> createUser(@Valid @RequestBody UserDTO userDTO) {
        userDTO.setProvider(Providers.SELF);
        if (userDTO.getImageName().isEmpty()) {
            userDTO.setImageName("https://res-console.cloudinary.com/dfikzvebd/media_explorer_thumbnails/8b0789a5b6b0a31d118be5dd0e62e62a/detailed");
        }
        UserDTO user = userService.createUser(userDTO);
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }
    //update
    @PutMapping("/{userId}")
    @Operation(summary = "update user")
    public ResponseEntity<UserDTO> updateUser(
            @Valid @RequestBody UserDTO userDTO,
            @PathVariable("userId") String userId) {
        return new ResponseEntity<>(userService.UpdateUser(userDTO, userId), HttpStatus.OK);
    }
    //delete
    @DeleteMapping("/{userId}")
    @Operation(summary = "delete user")
    public ResponseEntity<ApiResponseMessage> deleteUser(
            @PathVariable("userId") String userId) throws IOException {

        userService.deleteUser(userId);

        return new ResponseEntity<>(ApiResponseMessage.builder()
                .success(true)
                .massage("User is deleted successfully")
                .httpStatus(HttpStatus.OK)
                .build(), HttpStatus.OK);

    }
    //get All
    @GetMapping
    @Operation(summary = "get all users")
    public ResponseEntity<PageableResponse<UserDTO>> getAllUsers(
            @RequestParam(value = "pageNumber",defaultValue = AppCon.Page_Number,required = false) int pageNumber,
            @RequestParam(value = "pageSize",defaultValue = AppCon.Page_Size,required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue=AppCon.Sort_By,required = false) String sortBy ,
            @RequestParam(value = "sortDir", defaultValue=AppCon.Sort_Dir,required = false) String sortDir)
    {
        return new ResponseEntity<>(userService
                .getAllUser(pageNumber,pageSize,sortBy,sortDir),
                HttpStatus.OK);
    }
    //get By id
    @GetMapping("/{userId}")
    @Operation(summary = "get single user")
    public ResponseEntity<UserDTO> getUserById(
            @PathVariable("userId") String userId) {
        return new ResponseEntity<>(userService
                .getUserById(userId),
                HttpStatus.OK);
    }
    //get By Email
    @GetMapping("/email/{email}")
    @Operation(summary = "get user by emailId")
    public ResponseEntity<UserDTO> getUserByEmail(
            @PathVariable("email") String email) {
        return new ResponseEntity<>(userService
                .getUserByEmail(email),
                HttpStatus.OK);
    }
    //search user
    @GetMapping("/search/{keyword}")
    @Operation(summary = "search user by key word")
    public ResponseEntity<PageableResponse<UserDTO>> searchUser(
            @PathVariable("keyword") String keyword,
            @RequestParam(value = "pageNumber",defaultValue = AppCon.Page_Number,required = false) int pageNumber,
            @RequestParam(value = "pageSize",defaultValue = AppCon.Page_Size,required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue=AppCon.Sort_By,required = false) String sortBy ,
            @RequestParam(value = "sortDir", defaultValue=AppCon.Sort_Dir,required = false) String sortDir) {
        return new ResponseEntity<>(
                userService
                        .searchUser(keyword, pageNumber, pageSize, sortBy, sortDir),
                HttpStatus.OK);
    }
    //upload image
    @PostMapping("/image/{userId}")
    public ResponseEntity<ImageResponse> uploadUserImage(@PathVariable("userId") String userId,
            @RequestParam("userImage")MultipartFile image) throws IOException{
           String imageName=fileService.uploadImage(image, imageUploadPath);
           logger.info("Image path name {}",imageName);
           UserDTO userDTO=userService.getUserById(userId);
           userDTO.setImageName(imageName);
           userService.UpdateUser(userDTO,userId);
            return  new ResponseEntity<>(
                    ImageResponse
                            .builder()
                            .imageName(imageName)
                            .massage("Image uploaded successfully")
                            .success(true)
                            .httpStatus(HttpStatus.CREATED)
                            .build(),
                    HttpStatus.CREATED
            );
    }
    //serve User Image
    @GetMapping("/image/{userId}")
    public void serveUserImage(@PathVariable("userId") String userId, HttpServletResponse response) throws IOException {
       UserDTO userDTO= userService.getUserById(userId);
        logger.info("User image name {}",userDTO.getImageName());
      InputStream resource=fileService.getResource(imageUploadPath,userDTO.getImageName());
      response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        StreamUtils.copy(resource,response.getOutputStream());

    }
}
