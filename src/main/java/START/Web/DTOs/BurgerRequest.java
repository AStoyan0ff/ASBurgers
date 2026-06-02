package START.Web.DTOs;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BurgerRequest {

    @NotBlank
    @Size(min = 3, max = 60)
    private String name;

    @NotBlank
    @Size(min = 10, max = 500)
    private String description;

    @NotBlank
    @Size(min = 10, max = 500)
    private String ingredients;

    @NotBlank
    private String imageURL;

    @NotNull
    @DecimalMin("1.00")
    private BigDecimal price;

    private boolean isAvailable = true;
}
