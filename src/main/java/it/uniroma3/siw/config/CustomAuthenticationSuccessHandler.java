package it.uniroma3.siw.config;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import it.uniroma3.siw.model.Credentials;
import it.uniroma3.siw.model.User;
import it.uniroma3.siw.service.CredentialsService;
import it.uniroma3.siw.service.UserService;

import static it.uniroma3.siw.model.Credentials.ADMIN_ROLE;

@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    private CredentialsService credentialsService;
    
    @Autowired
    private UserService userService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        
        String usernameFromAuth = authentication.getName(); 
        
        System.out.println("--- OAuth2 Login Success ---");
        System.out.println("Authenticated principal name (username): " + usernameFromAuth);

        if (authentication.getPrincipal() instanceof OAuth2User) {
            OAuth2User oauth2User = (OAuth2User) authentication.getPrincipal();
            String firstName = oauth2User.getAttribute("given_name");
            String lastName = oauth2User.getAttribute("family_name");
            if (!credentialsService.existsByUsername(usernameFromAuth)) {
                System.out.println("Nuovo utente OAuth2 rilevato. Inizio provisioning per: " + usernameFromAuth);
                
                User newUser = new User();
                newUser.setNome(firstName != null ? firstName : "Utente");
                newUser.setCognome(lastName != null ? lastName : "OAuth2");
                Credentials newCredentials = new Credentials();
                newCredentials.setUsername(usernameFromAuth);
                newCredentials.setPassword("OAUTH2_GENERATED_PASSWORD_" + System.currentTimeMillis()); 
                newCredentials.setRole(Credentials.DEFAULT_ROLE);
                newCredentials.setUser(newUser); 
                
                credentialsService.saveCredentials(newCredentials);
                System.out.println("Utente OAuth2 " + usernameFromAuth + " provisionato con successo nel database locale.");
            } else {
                System.out.println("Utente OAuth2 " + usernameFromAuth + " già esistente nel database locale.");
            }
        }
        if (authentication.getAuthorities().contains(new SimpleGrantedAuthority(ADMIN_ROLE))) {
            response.sendRedirect("/admin/dashboard");
        } else {
            response.sendRedirect("/");
        }
    }
}