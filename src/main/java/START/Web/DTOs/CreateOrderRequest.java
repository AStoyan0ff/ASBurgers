package START.Web.DTOs;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class CreateOrderRequest {

    @NotBlank
    @Size(min = 3, max = 120)
    private String deliveryAddress;

    @Valid
    @NotEmpty
    private List<OrderItemRequest> items;

}
