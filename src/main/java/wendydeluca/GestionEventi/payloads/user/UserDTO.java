package wendydeluca.GestionEventi.payloads.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UserDTO (@NotNull(message = "Name cannot be null")
                       @NotEmpty(message = "Name cannot be empty")
                       @Size(min = 3, max = 20, message = "Name must be between 3 and 30 chars")
                       String name,
                       @NotNull(message = "Surname cannot be null")
                       @NotEmpty(message = "Surname cannot be empty")
                       @Size(min = 3, max = 20, message = "Surname must be between 3 and 30 chars")
                       String surname,
                       @Email
                       @NotNull(message = "Email cannot be null")
                       @NotEmpty(message = "Email cannot be empty")
                       String email,
                       @NotNull(message = "Password cannot be null")
                       @NotEmpty(message = "Password cannot be empty")
                       @Size(min = 8, max = 18, message = "Password must be between 8 and 18 chars")
                       String password){
}
