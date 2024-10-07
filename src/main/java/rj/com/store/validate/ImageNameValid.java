package rj.com.store.validate;


import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;


@Target({ElementType.FIELD,ElementType.PARAMETER,ElementType.METHOD, ElementType.ANNOTATION_TYPE,ElementType.CONSTRUCTOR})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = ImageNameValidator.class)
public @interface ImageNameValid {
    //error massage
    String message() default "{Invalid Image Size}";
    //represent Group of constraints
    Class<?>[] groups() default { };
    //Additional information about annotation
    Class<? extends Payload>[] payload() default { };
    boolean checkEmpty() default true;


}
