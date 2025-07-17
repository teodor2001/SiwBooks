package it.uniroma3.siw.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import it.uniroma3.siw.service.LibroService;

@Controller
public class LibroController {
	@Autowired LibroService libroService;
	
    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("libri", this.libroService.getAllLibri());
        return "index.html";
    }
	
	@GetMapping("/libro/{id}")
	public String getLibro(@PathVariable("id") Long id, Model model)
	{
		model.addAttribute("libro",this.libroService.getLibroById(id));
		return "libro.html";
	}
	
	@GetMapping("/libri")
	public String showLibri(Model model)
	{
		model.addAttribute("libri", this.libroService.getAllLibri());
		return "libri.html";
	}
}
