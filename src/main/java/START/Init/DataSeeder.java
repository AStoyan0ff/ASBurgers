package START.Init;

import START.Enums.UserRole;
import START.Models.Burger;
import START.Models.User;
import START.Repositories.BurgerRepository;
import START.Repositories.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@Order(1)
public class DataSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final BurgerRepository burgerRepository;
    private final PasswordEncoder passwordEncoder;

    public DataSeeder(UserRepository userRepository,
                      BurgerRepository burgerRepository,
                      PasswordEncoder passwordEncoder) {

        this.userRepository = userRepository;
        this.burgerRepository = burgerRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {

        adminSeed();
        burgerSeed();
    }

    private void adminSeed() {

        if (userRepository.count() > 0) {
            return;
        }

        User admin = User.builder()
                .username("admin")
                .email("admin@burger.com")
                .password(passwordEncoder.encode("admin123"))
                .address("Sofia")
                .role(UserRole.ADMIN)
                .build();

        userRepository.save(admin);
    }

    private void burgerSeed() {

        removeBurgerIfExists("Bacon Burger");

        seedBurgerIfMissing(
                "Classic Burger",
                "Classic beef burger with fresh veggies",
                "Beef patty, cheddar, lettuce, tomato, onion",
                BigDecimal.valueOf(12.99),
                "https://images.unsplash.com/photo-1568901346375-23c9450c58cd?auto=format&fit=crop&w=800"
        );

        seedBurgerIfMissing(
                "Double Cheese Burger",
                "Two patties loaded with melted cheese",
                "Double beef, double cheddar, pickles, house sauce",
                BigDecimal.valueOf(16.99),
                "https://images.unsplash.com/photo-1586190848861-99aa4a171e90?auto=format&fit=crop&w=800"
        );

        seedBurgerIfMissing(
                "Crispy Chicken Burger",
                "Golden crispy chicken fillet with creamy slaw",
                "Crispy chicken, lettuce, tomato, mayo, brioche bun",
                BigDecimal.valueOf(14.49),
                "https://images.unsplash.com/photo-1606755962773-d324e0a13086?auto=format&fit=crop&w=800"
        );
    }

    private void removeBurgerIfExists(String name) {
        burgerRepository.findByName(name).ifPresent(burger -> {

            try {
                burgerRepository.delete(burger);

            } catch (Exception ex) {
                burger.setAvailable(false);
                burgerRepository.save(burger);
            }
        });
    }

    private void seedBurgerIfMissing(String name,
                                     String description,
                                     String ingredients,
                                     BigDecimal price,
                                     String imageUrl) {

        if (burgerRepository.existsByName(name)) {
            return;
        }

        Burger burger = Burger.builder()
                .name(name)
                .description(description)
                .ingredients(ingredients)
                .price(price)
                .imageURL(imageUrl)
                .available(true)
                .build();

        burgerRepository.save(burger);
    }
}
