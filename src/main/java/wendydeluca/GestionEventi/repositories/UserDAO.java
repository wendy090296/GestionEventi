package wendydeluca.GestionEventi.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import wendydeluca.GestionEventi.entities.User;

import java.util.Optional;
import java.util.UUID;
@Repository
public interface UserDAO extends JpaRepository<User, UUID> {
    boolean existsByEmail(String email);
    Optional<User> findByEmail(String email);

}
