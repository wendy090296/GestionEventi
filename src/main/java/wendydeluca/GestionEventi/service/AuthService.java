package wendydeluca.GestionEventi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import wendydeluca.GestionEventi.entities.Role;
import wendydeluca.GestionEventi.entities.User;
import wendydeluca.GestionEventi.exceptions.BadRequestException;
import wendydeluca.GestionEventi.exceptions.UnauthorizedException;
import wendydeluca.GestionEventi.payloads.user.UserDTO;
import wendydeluca.GestionEventi.payloads.user.UserLoginDTO;
import wendydeluca.GestionEventi.repositories.UserDAO;
import wendydeluca.GestionEventi.security.JWTTools;

import java.io.IOException;

@Service
public class AuthService {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder bcrypt;

    @Autowired
    private JWTTools jwtTools;

    @Autowired
    private UserDAO userDAO;

    public String authenticateUserAndGenerateToken(UserLoginDTO payload){
        // 1. Fare un controllo delle credenziali d'accesso dell'utente
        // 1.1 Verificare che l'email del db sia quella ricevuta nel payload
        User user = userService.findByEmail(payload.email());
        // 1.2 Verificare che la password del db corrisponda a quella in entrata nel payload
        if(user.getPassword().equals(payload.password())){
            // 2. Se i controlli sono OK, genera il token
            return this.jwtTools.createToken(user);
        } else{
            // 3. Se non sono ok, 401 (unauthorized)
            throw new UnauthorizedException("Invalid login credentials! Try again.");
        }
    }

    public User registerUser(UserDTO body) throws IOException {
        userDAO.findByEmail(body.email()).ifPresent(a -> {
            throw new BadRequestException("User with email " + a.getEmail() + " already exists");
        });
        User user = new User();
        user.setPassword(bcrypt.encode(body.password()));
        user.setName(body.name());
        user.setSurname(body.surname());
        user.setEmail(body.email());
        user.setRole(Role.USER);
        return userDAO.save(user);
    }
}
