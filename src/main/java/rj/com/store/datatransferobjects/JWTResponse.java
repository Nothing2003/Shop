package rj.com.store.datatransferobjects;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JWTResponse
{
    private String token;
    UserDTO user;
    private String refreshToken;
}
