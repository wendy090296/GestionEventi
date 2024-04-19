package wendydeluca.GestionEventi.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import wendydeluca.GestionEventi.payloads.ErrorResponseDTO;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@RestControllerAdvice
public class ExceptionsHandler {

    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST) // 400
    public ErrorResponseDTO handleBadRequest(BadRequestException ex) {
        if (ex.getErrorsList() != null) {
            String message = ex.getErrorsList().stream().map(error -> error.getDefaultMessage())
                    .collect(Collectors.joining(" . "));
            return new ErrorResponseDTO(message, LocalDateTime.now());
        }
        return new ErrorResponseDTO(ex.getMessage(), LocalDateTime.now());

    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND) //404
    public ErrorResponseDTO handleNotFound(NotFoundException ex) {
        return new ErrorResponseDTO(ex.getMessage(), LocalDateTime.now());
    }

//    @ExceptionHandler(UnauthorizedException.class)
//    @ResponseStatus(HttpStatus.UNAUTHORIZED) //401
//    public ErrorResponseDTO handleUnauthorized(UnauthorizedException ex){
//        return new ErrorResponseDTO((ex.getMessage()),LocalDateTime.now());
//    }
//
//    @ExceptionHandler(AccessDeniedException.class)
//    @ResponseStatus(HttpStatus.FORBIDDEN) //403
//    public ErrorResponseDTO handleForbidden(AccessDeniedException ex){
//        return new ErrorResponseDTO(("You cannot access this functionality!"),LocalDateTime.now());
//    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR) //500
    public ErrorResponseDTO handleServerSideError(Exception ex) {
        ex.printStackTrace(); //stampiamo a schermo l'errore per andare a risolverlo!
        return new ErrorResponseDTO("Internal server error", LocalDateTime.now());
    }


}
