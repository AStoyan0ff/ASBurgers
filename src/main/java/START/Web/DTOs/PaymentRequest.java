package START.Web.DTOs;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentRequest {

    @NotBlank
    @Size(min = 3, max = 60)
    private String cardHolderName;

    @NotBlank
    @Pattern(regexp = "\\d{16}")
    private String cardNumber;

    @NotBlank
    @Pattern(regexp = "\\d{3}")
    private String cvc;
}
