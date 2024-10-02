package rj.com.store.datatransferobjects;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import rj.com.store.enities.Providers;
import rj.com.store.validate.ImageNameValid;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class UserDTO {

    private String userId;
    @NotBlank(message = "Name is required")
    @Size(min = 3, message = "Minimum 3 character required")
    @Schema(name = "username",accessMode = Schema.AccessMode.READ_ONLY,description = "user name for Database")
    private String name;

    @Email(message = "Invalid Email")
    @NotBlank(message = "Email is required")
    private String email;
    @NotBlank(message = "Password is required")
    private String password;
    @Size(min = 4, max = 6, message = "Inavaid")
    private String gender;
    @NotBlank(message = "Write someting  about yourself")
    private String about;
//    @ImageNameValid
    private String imageName;
    private List<RoleDTO> roles=new ArrayList<>();
    @Enumerated(EnumType.STRING)
    private Providers provider=Providers.SELF;

}
