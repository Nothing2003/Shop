package rj.com.store.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import rj.com.store.datatransferobjects.ApiResponseMessage;
import rj.com.store.datatransferobjects.ImageRequest;
import rj.com.store.datatransferobjects.ImageResponse;
import rj.com.store.services.ImageServiceInCloud;

@RestController
@RequestMapping("/image//v1")
@SecurityRequirement(name = "scheme")
@Tag(name = "Image Controller ",description = "This is image Api for image operation")
public class ImageController {
    private final Logger logger= LoggerFactory.getLogger(ImageController.class);

    private final ImageServiceInCloud imageServiceInCloud;
    public ImageController(ImageServiceInCloud imageServiceInCloud){
        this.imageServiceInCloud=imageServiceInCloud;

    }

    @PostMapping("/upload")
    @Operation(summary = "Upload user")
    public ResponseEntity<ImageResponse> uploadImage(@Valid @RequestBody ImageRequest imageRequest) {

        return new ResponseEntity<>(ImageResponse.builder()
                .imageName(imageServiceInCloud.uploadImage(imageRequest.image))
                .massage("Image successfully uploaded")
                .success(true)
                .httpStatus(HttpStatus.CREATED)
                .build(),
                HttpStatus.CREATED);
    }
}
