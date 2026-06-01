package START.Web.DTOs;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class LoginRequest {

    @NotBlank
    private String username;

    @NotBlank
    private String password;
}
