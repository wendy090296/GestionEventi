package wendydeluca.GestionEventi.exceptions;

public class AccessDeniedException extends RuntimeException {
    public AccessDeniedException(String msg){
        super(msg);
    }
}
