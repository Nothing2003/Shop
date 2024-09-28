package rj.com.store.validate;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;


@Target({ElementType.FIELD,ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = ImageNameValidator.class)
public @interface ImageNameValid {
    //error massage
    String message() default "{Invalid Image name}";
    //represent Group of constraints
    Class<?>[] groups() default { };
    //Additional information about annotation
    Class<? extends Payload>[] payload() default { };


}
