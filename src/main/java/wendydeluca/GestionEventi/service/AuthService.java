package wendydeluca.GestionEventi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import wendydeluca.GestionEventi.entities.User;
import wendydeluca.GestionEventi.payloads.user.UserDTO;
import wendydeluca.GestionEventi.payloads.user.UserLoginDTO;

@Service
public class AuthService {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder bcrypt;

//    public String authenticateUser(UserLoginDTO payload){
//        User user = userService.findByEmail(payload.email());
//        if(bcrypt.matches(payload.password(), user.getPassword())){
//            return
//
//        }
//
//    }
}
