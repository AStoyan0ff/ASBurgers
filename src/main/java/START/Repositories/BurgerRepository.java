package START.Repositories;

import START.Models.Burger;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface BurgerRepository extends JpaRepository<Burger, UUID> {

    List<Burger> findAllByAvailableTrue();

    List<Burger> findAllByOrderByNameAsc();

    boolean existsByName(String name);

    Optional<Burger> findByName(String name);
}
