package it.uniroma3.siw.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import it.uniroma3.siw.model.Libro;
import it.uniroma3.siw.service.AutoreService;
import it.uniroma3.siw.service.LibroService;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private LibroService libroService;

    @Autowired
    private AutoreService autoreService;

    @GetMapping("/formNewLibro")
    public String formNewLibro(Model model) {
        model.addAttribute("libro", new Libro());
        model.addAttribute("autori", this.autoreService.findAll());
        return "admin/formNewLibro.html";
    }


    @PostMapping("/libri")
    public String newLibro(@Valid @ModelAttribute("libro") Libro libro, BindingResult bindingResult, Model model) {
        
        if (!bindingResult.hasErrors()) {
            this.libroService.save(libro);
            model.addAttribute("libro", libro);
            return "libro.html";
        } else {
            model.addAttribute("autori", this.autoreService.findAll());
            return "admin/formNewLibro.html";
        }
    }
}