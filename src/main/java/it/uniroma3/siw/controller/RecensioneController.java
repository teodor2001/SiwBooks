package it.uniroma3.siw.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
        
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Credentials credentials = credentialsService.findByUsername(userDetails.getUsername());
        
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

        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Credentials credentials = credentialsService.findByUsername(userDetails.getUsername());
        Libro libro = this.libroService.getLibroById(libroId);
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