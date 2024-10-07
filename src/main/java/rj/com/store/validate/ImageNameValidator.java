package rj.com.store.validate;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.web.multipart.MultipartFile;


public class ImageNameValidator implements ConstraintValidator<ImageNameValid, MultipartFile> {
    private static final long MAX_FILE_SIZE = 1024 * 1024 * 10; // 2MB
    @Override
    public boolean isValid(MultipartFile multipartFile, ConstraintValidatorContext constraintValidatorContext) {
        if (multipartFile == null || multipartFile.isEmpty()) {
            return false;
        }
        if (multipartFile.getSize() > MAX_FILE_SIZE) {
            constraintValidatorContext.disableDefaultConstraintViolation();
            constraintValidatorContext.buildConstraintViolationWithTemplate("File size should be less them 2MB").addConstraintViolation();
            return false;
        }
        return true;
    }
}
