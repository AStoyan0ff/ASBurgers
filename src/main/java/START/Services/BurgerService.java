package START.Services;

import START.Exception.BurgerNotFoundException;
import START.Models.Burger;
import START.Repositories.BurgerRepository;
import START.Web.DTOs.BurgerRequest;
import START.Web.DTOs.BurgerResponse;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class BurgerService {

    private final BurgerRepository burgerRepository;

    public BurgerService(BurgerRepository burgerRepository) {
        this.burgerRepository = burgerRepository;
    }

    public List<BurgerResponse> getAllAvailableBurgers() {
        return burgerRepository.findAllByAvailableTrue()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public Burger getById(UUID id) {
        return burgerRepository.findById(id).orElseThrow(BurgerNotFoundException::new);
    }

    public void createBurger(BurgerRequest request) {
        Burger burger = Burger.builder()
                .name(request.getName())
                .description(request.getDescription())
                .ingredients(request.getIngredients())
                .price(request.getPrice())
                .imageURL(request.getImageURL())
                .available(request.isAvailable())
                .build();

        burgerRepository.save(burger);
    }

    public void delete(UUID id) {
        burgerRepository.deleteById(id);
    }

    public BurgerRequest getBurgerRequestById(UUID id) {
        Burger burger = getById(id);

        return BurgerRequest.builder()
                .name(burger.getName())
                .description(burger.getDescription())
                .ingredients(burger.getIngredients())
                .price(burger.getPrice())
                .imageURL(burger.getImageURL())
                .isAvailable(burger.isAvailable())
                .build();
    }

    private BurgerResponse mapToResponse(Burger burger) {

        BurgerResponse response = BurgerResponse.builder()
                .id(burger.getId())
                .name(burger.getName())
                .description(burger.getDescription())
                .ingredients(burger.getIngredients())
                .price(burger.getPrice())
                .imageUrl(burger.getImageURL())
                .available(burger.isAvailable())
                .build();

        return response;
    }
}
