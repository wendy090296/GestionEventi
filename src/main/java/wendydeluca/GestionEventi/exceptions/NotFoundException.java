package wendydeluca.GestionEventi.exceptions;

import java.util.UUID;

public class NotFoundException extends RuntimeException{
    public NotFoundException(String message){
        super(message);
    }

    public NotFoundException(UUID id){
        super("The user with id" + id + "has not been found!");
    }

}
