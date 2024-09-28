package rj.com.store.services;

import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

public interface ImageServiceInCloud {
    String uploadImage(MultipartFile image);
    String getURLFromPublicId(String publicID);
    String deleteImage(String publicId);
}
