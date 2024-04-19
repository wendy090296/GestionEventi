package wendydeluca.GestionEventi.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import wendydeluca.GestionEventi.entities.Event;
import wendydeluca.GestionEventi.entities.User;

import java.util.UUID;

@Repository
public interface EventDAO extends JpaRepository<Event, UUID> {
public Page<Event> findByUsers(User user, Pageable pageable);
}
