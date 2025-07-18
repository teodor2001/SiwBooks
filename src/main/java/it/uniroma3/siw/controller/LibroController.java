package it.uniroma3.siw.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
	
    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("libri", this.libroService.getAllLibri());
        return "index.html";
    }
	
    @GetMapping("/libro/{id}")
	public String getLibro(@PathVariable("id") Long id, Model model) {
		Libro libro = this.libroService.getLibroById(id);
		model.addAttribute("libro", libro);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean hasReviewed = false;
        
        if (authentication != null && authentication.isAuthenticated() && !(authentication.getPrincipal() instanceof String)) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            Credentials credentials = credentialsService.findByUsername(userDetails.getUsername());
            if (libro != null && credentials != null) {
                hasReviewed = recensioneService.hasUserReviewedBook(libro, credentials.getUser());
            }
        }
        model.addAttribute("hasReviewed", hasReviewed);

		return "libro.html";
	}
	
}