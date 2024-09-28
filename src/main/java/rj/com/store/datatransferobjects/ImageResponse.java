package rj.com.store.datatransferobjects;

import lombok.*;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ImageResponse {
    private String imageName;
    private String massage;
    private boolean success;
    private HttpStatus httpStatus;


}
