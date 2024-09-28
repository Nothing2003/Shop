package rj.com.store.services.servicesimp;

import com.cloudinary.Cloudinary;
import com.cloudinary.Transformation;
import com.cloudinary.utils.ObjectUtils;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import rj.com.store.services.ImageServiceInCloud;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;
import org.slf4j.Logger;

@Service
public class ImageServiceInCloudImp implements ImageServiceInCloud {
    Logger logger= LoggerFactory.getLogger(ImageServiceInCloudImp.class);
    @Autowired
   private Cloudinary cloudinary;
    @Override
    public String uploadImage(MultipartFile image) {
        if (image.isEmpty()) {
            return "https://res-console.cloudinary.com/dfikzvebd/media_explorer_thumbnails/9dcdb7c30711d66f219c1d8c14adf9cc/detailed";
        }
        try {
            byte[] data = new byte[image.getInputStream().available()];
            String fileName = UUID.randomUUID().toString();
            logger.info("File name {}",fileName);
            image.getInputStream().read(data);
            cloudinary.uploader().upload(data, ObjectUtils.asMap(
                    "public_id", fileName
            ));
            return this.getURLFromPublicId(fileName);
        } catch (IOException e) {
            return null;
        }
    }
    @Override
    public String getURLFromPublicId(String publicID) {
        logger.info("File name {}",publicID);
        return cloudinary.url().transformation(
                new Transformation<>()
        ).generate(publicID);
    }
    public String deleteImage(String publicId) {
        if (publicId.isEmpty()||publicId.equalsIgnoreCase(" ")){
            return "Ok";
        }
        try {
            String im=publicId.substring(publicId.lastIndexOf("/")+1,publicId.lastIndexOf("."));
            logger.info("Delete file public id {}",im);
            cloudinary.uploader().destroy(im,ObjectUtils.asMap(
                    "public_id",im
            ));
            return "Image Successfully delete";
        } catch (Exception e) {
            throw new RuntimeException("Image Not Delete");
        }
    }
}
