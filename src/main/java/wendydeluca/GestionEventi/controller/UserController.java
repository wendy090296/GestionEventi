package wendydeluca.GestionEventi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import wendydeluca.GestionEventi.entities.Event;
import wendydeluca.GestionEventi.entities.User;
import wendydeluca.GestionEventi.exceptions.BadRequestException;
import wendydeluca.GestionEventi.payloads.user.UserDTO;
import wendydeluca.GestionEventi.service.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import java.util.UUID;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping
    @PreAuthorize("hasAuthority('EVENT_MANAGER')") // concedo solo all'admin il permesso di visionare la lista utenti dell'evento
    public Page<User> getAllUsers(@RequestParam(defaultValue = "0") int page,
                                  @RequestParam(defaultValue = "10") int size,
                                  @RequestParam(defaultValue = "name") String sortBy){
        return userService.findAllUsers(page,size,sortBy);
    }

    @GetMapping("/{userId}")
    @PreAuthorize("hasAuthority('EVENT_MANAGER')")
    public User findUserById(@PathVariable UUID userId){
        return userService.findById(userId);

    }

    @PostMapping("/{userId}")
    @PreAuthorize("hasAuthority('EVENT_MANAGER')")
    public User saveUser(@PathVariable UUID userId, @RequestBody @Validated UserDTO body, BindingResult validation){
        if(validation.hasErrors()){
            System.out.println(validation.getAllErrors());
            throw new BadRequestException(validation.getAllErrors());
        }
        return userService.findByIdAndUpdate(userId,body);
    }

    @PutMapping("/{userId}")
    @PreAuthorize("hasAuthority('EVENT_MANAGER')")
    public User findUserByIdAndUpdate(@PathVariable UUID userId, @RequestBody @Validated UserDTO body, BindingResult validation){
        if(validation.hasErrors()){
            System.out.println(validation.getAllErrors());
            throw new BadRequestException(validation.getAllErrors());
        }
        return userService.findByIdAndUpdate(userId,body);
    }



    @DeleteMapping("/{userId}")
    @PreAuthorize("hasAuthority('EVENT_MANAGER')")
    public void deleteUser(@PathVariable UUID userId){
        userService.deleteUser(userId);

    }


    @GetMapping("/me")
    public User getProfile(@AuthenticationPrincipal User currentAuthUser){
        return currentAuthUser;
    }

    @PutMapping("/me")
    public User updateProfile(@AuthenticationPrincipal User currentAuthUser, @RequestBody UserDTO updateUser){
        return userService.findByIdAndUpdate(currentAuthUser.getId(),updateUser);
    }


    @DeleteMapping("/me")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProfile(@AuthenticationPrincipal User currentAuthUser){
        this.userService.deleteUser(currentAuthUser.getId());
    }


    @GetMapping("/me/events")
    public Page<Event> getEvents(@AuthenticationPrincipal User user,
                                 @RequestParam(defaultValue = "0") int page,
                                 @RequestParam(defaultValue = "10") int size,
                                 @RequestParam(defaultValue = "name") String sortBy) {
        return userService.getEvents(user,page,size,sortBy);

    }



   @GetMapping("/me/events/{id}/book")
    public Event bookEvent(@AuthenticationPrincipal User user, @PathVariable UUID id){
        return userService.bookEvent(user.getId(),id);
}

    @GetMapping("/me/events/{id}/unbook")
    public Event unbookEvent(@AuthenticationPrincipal User user, @PathVariable UUID id){
        return userService.unbookEvent(user.getId(),id);
    }







}


