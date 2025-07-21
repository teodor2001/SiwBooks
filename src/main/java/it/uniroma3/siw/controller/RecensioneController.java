package it.uniroma3.siw.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User; // Importa OAuth2User
import it.uniroma3.siw.model.Credentials;
import it.uniroma3.siw.model.Libro;
import it.uniroma3.siw.model.Recensione;
import it.uniroma3.siw.service.CredentialsService;
import it.uniroma3.siw.service.LibroService;
import it.uniroma3.siw.service.RecensioneService;
import jakarta.validation.Valid;

@Controller
public class RecensioneController {

    @Autowired private LibroService libroService;
    
    @Autowired private RecensioneService recensioneService;
    
    @Autowired private CredentialsService credentialsService;

    @GetMapping("/recensione/add/{libroId}")
    public String showFormNewRecensione(@PathVariable("libroId") Long libroId, Model model, RedirectAttributes redirectAttributes) {
        Libro libro = this.libroService.getLibroById(libroId);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String usernameToLookup = authentication.getName(); 
        if (authentication.getPrincipal() instanceof OAuth2User) {
            OAuth2User oauth2User = (OAuth2User) authentication.getPrincipal();
            String email = oauth2User.getAttribute("email");
            if (email != null && !email.isEmpty()) {
                usernameToLookup = email;
            }
        }

        Credentials credentials = credentialsService.findByUsername(usernameToLookup);
        if (credentials == null) {
            redirectAttributes.addFlashAttribute("error", "Errore: utente non trovato o non autenticato correttamente per la recensione.");
            return "redirect:/login"; 
        }

        if (recensioneService.hasUserReviewedBook(libro, credentials.getUser())) {
            redirectAttributes.addFlashAttribute("reviewError", "Hai già scritto una recensione per questo libro.");
            return "redirect:/libro/" + libroId;
        }

        model.addAttribute("libro", libro);
        model.addAttribute("recensione", new Recensione());
        return "formNewRecensione.html";
    }

    @PostMapping("/recensione/add/{libroId}")
    public String saveRecensione(@Valid @ModelAttribute("recensione") Recensione recensione,
                                 BindingResult bindingResult,
                                 @PathVariable("libroId") Long libroId, Model model, RedirectAttributes redirectAttributes) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String usernameToLookup = authentication.getName(); 
        if (authentication.getPrincipal() instanceof OAuth2User) {
            OAuth2User oauth2User = (OAuth2User) authentication.getPrincipal();
            String email = oauth2User.getAttribute("email");
            if (email != null && !email.isEmpty()) {
                usernameToLookup = email;
            }
        }

        Credentials credentials = credentialsService.findByUsername(usernameToLookup);
        Libro libro = this.libroService.getLibroById(libroId);
        if (credentials == null) {
            redirectAttributes.addFlashAttribute("error", "Errore: utente non trovato o non autenticato correttamente per la recensione.");
            return "redirect:/login";
        }

        if (recensioneService.hasUserReviewedBook(libro, credentials.getUser())) {
            redirectAttributes.addFlashAttribute("reviewError", "Hai già scritto una recensione per questo libro.");
            return "redirect:/libro/" + libroId;
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("libro", libro);
            return "formNewRecensione.html";
        }
        
        recensione.setUtente(credentials.getUser());
        recensione.setLibro(libro);
        this.recensioneService.save(recensione);
        
        redirectAttributes.addFlashAttribute("reviewSuccess", "La tua recensione è stata pubblicata!");
        return "redirect:/libro/" + libroId;
    }
}