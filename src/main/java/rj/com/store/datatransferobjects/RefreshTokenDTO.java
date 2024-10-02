package rj.com.store.datatransferobjects;

import jakarta.persistence.OneToOne;
import lombok.*;

import java.time.Instant;
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RefreshTokenDTO {
        private int id;
        private String token;
        private Instant expiresDate;
}
