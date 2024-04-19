package wendydeluca.GestionEventi.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import wendydeluca.GestionEventi.entities.Event;

import java.util.UUID;

@Repository
public interface EventDAO extends JpaRepository<Event, UUID> {

}
