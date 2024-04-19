package wendydeluca.GestionEventi.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import wendydeluca.GestionEventi.entities.User;
import wendydeluca.GestionEventi.exceptions.UnauthorizedException;

import java.util.Date;

@Component
public class JWTTools {

    @Value("${jwt.secret}")
    private String secret;
    public String createToken(User user){
        return Jwts.builder()
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24))
                .subject(String.valueOf(user.getId()))
                .signWith(Keys.hmacShaKeyFor(this.secret.getBytes()
                ))
                .compact();
    }


    public void verifyToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(Keys.hmacShaKeyFor(this.secret.getBytes()))
                    .build().parse(token);
            // per poterlo verificare, devo passargli il segreto!
        } catch (Exception ex) {
            throw new UnauthorizedException("Token issues! Try again to login.");
            // il token potrebbe essere o scaduto o manipolato, in ogni caso --> 401. (unauthorized)
        }
    }

    // estraggo l'id dal token

    public String extractIdFromToken(String token){
        return   Jwts.parser()
                .verifyWith(Keys.hmacShaKeyFor(this.secret.getBytes()))
                .build().parseEncryptedClaims(token).getPayload().getSubject();

    }


}

