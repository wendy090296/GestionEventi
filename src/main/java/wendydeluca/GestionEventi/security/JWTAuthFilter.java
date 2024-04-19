package wendydeluca.GestionEventi.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;
import wendydeluca.GestionEventi.entities.User;
import wendydeluca.GestionEventi.exceptions.UnauthorizedException;
import wendydeluca.GestionEventi.service.UserService;
import org.springframework.security.core.Authentication;

import java.io.IOException;
import java.util.UUID;

public class JWTAuthFilter extends OncePerRequestFilter {
    @Autowired
    private JWTTools jwtTools;

    @Autowired
    private UserService userService;
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {


        // Vedo se é presente il token nell'header Authorizaion

        String authHeader = request.getHeader("Authorization");
        if(authHeader == null || !authHeader.startsWith("Bearer")) throw new UnauthorizedException("Insert the token please.");
        // 2. lo estraggo (se presente)
        String accessToken = authHeader.substring(7);
        // 3. verifico la sua integrità
        this.jwtTools.verifyToken(accessToken);
        // 4. se tutto é ok, si procede nella filter chain
        // 4.1 cerco l'utente nel db tramite l'id

        String id= jwtTools.extractIdFromToken(accessToken);
        User currentUser = this.userService.findById(UUID.fromString(id));

        // 4.2 informo spring security il ruolo dello user che sta effettuando la richiesta

        Authentication authentication = new UsernamePasswordAuthenticationToken(currentUser,null,currentUser.getAuthorities());
        //getAuthorities ci consente di accedere alla lista dei ruoli dello user
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // 4.3 procede al prossimo filtro della catena
        filterChain.doFilter(request,response);

        // se il token non é ok, --> 401.



    }

    // non voglio che il filtro entri in azione in fase di registrazione e login!
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request){
        return new AntPathMatcher().match("/auth/**",request.getServletPath());
    }




}

