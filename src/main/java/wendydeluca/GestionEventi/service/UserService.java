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
import wendydeluca.GestionEventi.exceptions.BadRequestException;
import wendydeluca.GestionEventi.exceptions.NotFoundException;
import wendydeluca.GestionEventi.payloads.user.UserDTO;
import wendydeluca.GestionEventi.repositories.UserDAO;

import java.util.UUID;

@Service
public class UserService {

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private PasswordEncoder bcrypt;

    public Page<User> findAllUsers(int page, int size, String sortBy) {
        if (size < 50) size = 50;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return userDAO.findAll(pageable);
    }

    public User findById(UUID userId){
        return userDAO.findById(userId).orElseThrow(()->new NotFoundException(userId));
    }

    public User findByEmail(String email){
        return userDAO.findByEmail(email).orElseThrow(()-> new NotFoundException("User with email " + email + " not found!"));
    }


    public User saveNewUser(UserDTO payload){
        // 1. Se l'email dello user non é presente,
        if (!userDAO.existsByEmail(payload.email())) {
            // 2. creo un nuovo oggetto User "modellato" sul payload
            User newUser = new User(payload.name(), payload.surname(), payload.email(),bcrypt.encode(payload.password()));
            return userDAO.save(newUser);
            // Se é già presente, lancio eccezione :
        } else throw new BadRequestException("User with email '" + payload.email() + "  already exists.");

    }

    public User findByIdAndUpdate(UUID userId, UserDTO payload){
        User found = this.findById(userId);
        found.setName(payload.name());
        found.setSurname(payload.surname());
        found.setEmail(payload.email());
        userDAO.save(found);
        return found;

    }

    public void deleteUser(UUID userId){
        User found = this.findById(userId);
        userDAO.delete(found);
    }

    public User setAdmin(UUID id){
        User user = userDAO.findById(id).orElseThrow(()-> new NotFoundException(id));
        user.setRole(Role.EVENT_MANAGER);
        return userDAO.save(user);
    }

    
}
