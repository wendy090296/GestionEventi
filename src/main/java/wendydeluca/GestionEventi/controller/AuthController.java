package wendydeluca.GestionEventi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import wendydeluca.GestionEventi.entities.User;
import wendydeluca.GestionEventi.exceptions.BadRequestException;
import wendydeluca.GestionEventi.payloads.user.UserDTO;
import wendydeluca.GestionEventi.payloads.user.UserLoginDTO;
import wendydeluca.GestionEventi.payloads.user.UserLoginResponseDTO;
import wendydeluca.GestionEventi.payloads.user.UserResponseDTO;
import wendydeluca.GestionEventi.service.AuthService;
import wendydeluca.GestionEventi.service.UserService;

import java.io.IOException;

@RestController
@RequestMapping("/auth")
public class AuthController {


    @Autowired
    private AuthService authService;


    @PostMapping("/register") //SAVE
    @ResponseStatus(HttpStatus.CREATED) //STATUS 201 OK
    public UserResponseDTO createUser(@RequestBody @Validated UserDTO newUserPayload, BindingResult validation) throws IOException {
        if(validation.hasErrors()){
            System.out.println(validation.getAllErrors());
            throw new BadRequestException(validation.getAllErrors());
        }else {
            User newUser = authService.registerUser(newUserPayload);

            return new UserResponseDTO(newUser.getId());
        }

    }


    @PostMapping("/login")
    public UserLoginResponseDTO login(@RequestBody UserLoginDTO body){
        String accessToken = authService.authenticateUserAndGenerateToken(body);
        return new UserLoginResponseDTO(this.authService.authenticateUserAndGenerateToken(body));

    }
}
