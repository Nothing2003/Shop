package rj.com.store.datatransferobjects;

import org.springframework.web.multipart.MultipartFile;
import rj.com.store.validate.ImageNameValid;

public class ImageRequest {
    @ImageNameValid
    public MultipartFile image;
}
