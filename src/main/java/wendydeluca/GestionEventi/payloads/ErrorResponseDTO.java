package wendydeluca.GestionEventi.payloads;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

public record ErrorResponseDTO (String message, LocalDateTime timestamp){
}
