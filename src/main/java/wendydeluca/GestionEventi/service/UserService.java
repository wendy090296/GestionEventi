package wendydeluca.GestionEventi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import wendydeluca.GestionEventi.entities.Event;
import wendydeluca.GestionEventi.entities.Role;
import wendydeluca.GestionEventi.entities.User;
import wendydeluca.GestionEventi.exceptions.AttendanceException;
import wendydeluca.GestionEventi.exceptions.BadRequestException;
import wendydeluca.GestionEventi.exceptions.NotFoundException;
import wendydeluca.GestionEventi.payloads.user.UserDTO;
import wendydeluca.GestionEventi.repositories.EventDAO;
import wendydeluca.GestionEventi.repositories.UserDAO;

import java.util.UUID;

@Service
public class UserService {

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private PasswordEncoder bcrypt;

    @Autowired
    private EventDAO eventDAO;

    public Page<User> findAllUsers(int page, int size, String sortBy) {
        if (size < 50) size = 50;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return userDAO.findAll(pageable);
    }

    public User findById(UUID userId) {
        return userDAO.findById(userId).orElseThrow(() -> new NotFoundException(userId));
    }

    public User findByEmail(String email) {
        return userDAO.findByEmail(email).orElseThrow(() -> new NotFoundException("User with email " + email + " not found!"));
    }


    public User saveNewUser(UserDTO payload) {
        // 1. Se l'email dello user non é presente,
        if (!userDAO.existsByEmail(payload.email())) {
            // 2. creo un nuovo oggetto User "modellato" sul payload
            User newUser = new User(payload.name(), payload.surname(), payload.email(), bcrypt.encode(payload.password()));
            return userDAO.save(newUser);
            // Se é già presente, lancio eccezione :
        } else throw new BadRequestException("User with email '" + payload.email() + "  already exists.");

    }

    public User findByIdAndUpdate(UUID userId, UserDTO payload) {
        User found = this.findById(userId);
        found.setName(payload.name());
        found.setSurname(payload.surname());
        found.setEmail(payload.email());
        userDAO.save(found);
        return found;

    }

    public void deleteUser(UUID userId) {
        User found = this.findById(userId);
        userDAO.delete(found);
    }

    public User setAdmin(UUID id) {
        User user = userDAO.findById(id).orElseThrow(() -> new NotFoundException(id));
        user.setRole(Role.EVENT_MANAGER);
        return userDAO.save(user);
    }

    public Event bookEvent(UUID userId, UUID eventId) {
        Event event = eventDAO.findById(eventId).orElseThrow(() -> new NotFoundException(eventId));
        User user = userDAO.findById(userId).orElseThrow(() -> new NotFoundException(userId));

        if (event.getUsers().contains(user)) {
            throw new AttendanceException(event, userId);
        } else {
            if (event.getUsers().size() < event.getAvailableSeats()) {
                event.getUsers().add(user);
                return eventDAO.save(event);
            } else {
                throw new AttendanceException("EVENT " + event.getTitle() + " SOLD OUT!");
            }

        }
    }

    public Event unbookEvent(UUID eventId, UUID userId) {
        Event event = eventDAO.findById(eventId).orElseThrow(() -> new NotFoundException(eventId));
        User user = userDAO.findById(userId).orElseThrow(() -> new NotFoundException(userId));

        if (event.getUsers().contains(user)) {
            event.getUsers().remove(user);
            return eventDAO.save(event);
        } else {
            throw new AttendanceException("USER ALREADY BOOKED IN!");
        }

    }

    public Page<Event> getEvents(User user, int page, int size, String sort){
        Pageable pageable = PageRequest.of(page,size,Sort.by(sort));
        return eventDAO.findByUsers(user,pageable);
}




}
