package it.uniroma3.siw.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import static it.uniroma3.siw.model.Credentials.ADMIN_ROLE;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(authorize -> authorize
            	//Se sei admin fai un po' come ti pare
                .requestMatchers("/admin/**").hasAuthority(ADMIN_ROLE)

                //Per le recensioni basta autenticarsi
                .requestMatchers("/recensione/add/**").authenticated()
                
                //Risorse accessibili a chiunque
                .requestMatchers("/", "/libri", "/libro/**", "/autori", "/autore/**").permitAll()
                .requestMatchers("/css/**", "/images/**").permitAll()
                .requestMatchers("/oauth2/**", "/login", "/register").permitAll() // Aggiunto /register qui
                .anyRequest().authenticated()
            )
            
            .formLogin(formLogin -> formLogin
                .loginPage("/login")
                .defaultSuccessUrl("/", true)
            )
            .oauth2Login(oauth2 -> oauth2
                .loginPage("/login")
                .defaultSuccessUrl("/", true)
            )
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
                .permitAll()
            );
        return http.build();
    }
}