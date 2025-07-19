package it.uniroma3.siw.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import it.uniroma3.siw.model.Autore;
import it.uniroma3.siw.service.AutoreService;
import it.uniroma3.siw.service.LibroService;

@Controller
public class AutoreController {
    
    @Autowired 
    private AutoreService autoreService;
    
    @Autowired
    private LibroService libroService;
    
    @GetMapping("/autore/{id}")
    public String getAutore(@PathVariable("id") Long id, Model model) {
        Autore autore = this.autoreService.findById(id);
        model.addAttribute("autore", autore);
        model.addAttribute("libri", this.libroService.findLibriByAutore(autore));
        return "autore.html";
    }
}