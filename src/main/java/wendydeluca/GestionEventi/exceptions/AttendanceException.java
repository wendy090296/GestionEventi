package wendydeluca.GestionEventi.exceptions;

import lombok.Getter;
import wendydeluca.GestionEventi.entities.Event;
import wendydeluca.GestionEventi.entities.User;

import java.util.UUID;

@Getter
public class AttendanceException extends RuntimeException {
    public AttendanceException(String msg){
        super(msg);
    }

    public AttendanceException(Event event, UUID userId){
        super("The user with id " + userId + "has already booked the event " + event.getTitle());
    }
}
