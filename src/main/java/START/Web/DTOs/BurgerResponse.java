package START.Web.DTOs;


import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
public class BurgerResponse {

    private UUID id;
    private String name;
    private String description;
    private String ingredients;
    private BigDecimal price;
    private String imageUrl;
    private boolean available;
}
