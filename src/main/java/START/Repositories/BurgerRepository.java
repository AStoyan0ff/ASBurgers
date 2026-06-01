package START.Repositories;

import START.Models.Burger;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface BurgerRepository extends JpaRepository<Burger, UUID> {

    List<Burger> findAllByAvailableTrue();
    boolean existsByName(String name);
}
