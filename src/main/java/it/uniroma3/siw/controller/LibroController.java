package it.uniroma3.siw.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.security.core.Authentication;

import it.uniroma3.siw.model.Credentials;
import it.uniroma3.siw.model.Libro;
import it.uniroma3.siw.service.CredentialsService;
import it.uniroma3.siw.service.LibroService;
import it.uniroma3.siw.service.RecensioneService;

@Controller
public class LibroController {
	
	@Autowired private LibroService libroService;
	
	@Autowired private RecensioneService recensioneService;
	
    @Autowired private CredentialsService credentialsService;
	
    @GetMapping({"/", "/libri"})
    public String index(Model model,
                        @RequestParam(value = "keyword", required = false) String keyword,
                        @RequestParam(value = "annoPubblicazione", required = false) Integer annoPubblicazione) {
        if ((keyword != null && !keyword.isEmpty()) || annoPubblicazione != null) {
            model.addAttribute("libri", this.libroService.searchAndFilterLibri(keyword, annoPubblicazione));
        } else {
            model.addAttribute("libri", this.libroService.getAllLibri());
        }
        model.addAttribute("keyword", keyword);
        model.addAttribute("annoPubblicazione", annoPubblicazione);

        return "index.html";
    }
	
    @GetMapping("/libro/{id}")
   	public String getLibro(@PathVariable("id") Long id, Model model) {
   		Libro libro = this.libroService.getLibroById(id);
   		model.addAttribute("libro", libro);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean hasReviewed = false;
        
        if (authentication != null && authentication.isAuthenticated() && !("anonymousUser").equals(authentication.getPrincipal())) {
            String usernameToLookup = authentication.getName(); 
            if (authentication.getPrincipal() instanceof OAuth2User) {
                OAuth2User oauth2User = (OAuth2User) authentication.getPrincipal();
                String email = oauth2User.getAttribute("email");
                if (email != null && !email.isEmpty()) {
                    usernameToLookup = email;
                }
            }

            Credentials credentials = credentialsService.findByUsername(usernameToLookup); 
            if (libro != null && credentials != null && credentials.getUser() != null) {
                hasReviewed = recensioneService.hasUserReviewedBook(libro, credentials.getUser());
            }
        }
        model.addAttribute("hasReviewed", hasReviewed);

   		return "libro.html";
   	}
   	
}