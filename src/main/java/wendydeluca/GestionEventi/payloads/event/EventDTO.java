package wendydeluca.GestionEventi.payloads.event;

import jakarta.validation.constraints.*;

public record EventDTO (@NotNull(message = "Title cannot be null")
                        @NotEmpty(message = "Title cannot be empty")
                        @Size(min = 3, max = 20, message = "Title must be between 3 and 30 chars")
                        String title,
                        @NotNull(message = "Description cannot be null")
                        @NotEmpty(message = "Description cannot be empty")
                        @Size(min = 3, max = 20, message = "Description must be between 3 and 30 chars")
                        String description,
                        @NotNull(message = "Location cannot be null")
                        @NotEmpty(message = "Location cannot be empty")
                        @Size(min = 3, max = 20, message = "Location must be between 3 and 30 chars")
                        String location,
                        @NotNull(message = "Location cannot be null")
                        @NotEmpty(message = "Location cannot be empty")
                        String date,
                        @Max(value= 5000, message = "The number of places available for the event are 5000!")
                        int availableSeats){
}
