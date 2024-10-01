package rj.com.store.datatransferobjects;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JWTRequest {
    private String email;
    private String password;
}
