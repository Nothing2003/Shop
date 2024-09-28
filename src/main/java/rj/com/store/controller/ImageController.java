package rj.com.store.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import rj.com.store.datatransferobjects.ApiResponseMessage;
import rj.com.store.datatransferobjects.ImageResponse;
import rj.com.store.services.ImageServiceInCloud;

@RestController
@RequestMapping("/image")
public class ImageController {
    private final Logger logger= LoggerFactory.getLogger(CategoryController.class);

    private final ImageServiceInCloud imageServiceInCloud;
    public ImageController(ImageServiceInCloud imageServiceInCloud){
        this.imageServiceInCloud=imageServiceInCloud;

    }
    @PostMapping("/upload")
    public ResponseEntity<ImageResponse> uploadImage(
            @RequestParam("Image") MultipartFile image){

        return new ResponseEntity<>(ImageResponse.builder()
                .imageName(imageServiceInCloud.uploadImage(image))
                .massage("Image successfully uploaded")
                .success(true)
                .httpStatus(HttpStatus.CREATED)
                .build()
        , HttpStatus.CREATED);
    }


}
