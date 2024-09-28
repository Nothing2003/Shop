package rj.com.store.services.servicesimp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import rj.com.store.exceptions.BadApiRequest;
import rj.com.store.services.FileService;

import java.io.InputStream;
import java.io.IOException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;
@Service
public class FileServiceImp implements FileService {
   private final Logger logger= LoggerFactory.getLogger(FileServiceImp.class);
    @Override
    public String uploadImage(MultipartFile file, String path) throws IOException {

        String originalFileName= file.getOriginalFilename();
        logger.info("Original File Name {}",originalFileName);
        String fileExtension=originalFileName.substring(originalFileName.lastIndexOf("."));
        logger.info("File extension {}",fileExtension);
        String fileWithoutPath=UUID.randomUUID().toString()+fileExtension;
        String fullPathWithFileName= path+fileWithoutPath;
        logger.info("File Path with name {}",fullPathWithFileName);
        if (fileExtension.equalsIgnoreCase(".png")||fileExtension.equalsIgnoreCase(".jpg")||fileExtension.equalsIgnoreCase(".jpeg")){
            File folder=new File(path);
            if(!folder.exists()){
                folder.mkdirs();
            }
            Files.copy(file.getInputStream(), Paths.get(fullPathWithFileName));
            return fileWithoutPath;
        }else {
                throw new BadApiRequest("File with this "+fileExtension+" not allowed");
        }
    }

    @Override
    public InputStream getResource(String path, String name) throws FileNotFoundException {
        return new FileInputStream(path+name);
    }
}
