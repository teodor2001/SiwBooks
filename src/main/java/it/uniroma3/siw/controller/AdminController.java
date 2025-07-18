package it.uniroma3.siw.controller;

import java.io.IOException;

import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import it.uniroma3.siw.utility.FileUploadUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import it.uniroma3.siw.model.Autore;
import it.uniroma3.siw.model.Libro;
import it.uniroma3.siw.service.AutoreService;
import it.uniroma3.siw.service.LibroService;
import it.uniroma3.siw.service.RecensioneService;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private LibroService libroService;

    @Autowired
    private AutoreService autoreService;
    
    @Autowired private RecensioneService recensioneService;
 
    @GetMapping("/dashboard")
    public String showDashboard() {
        return "admin/dashboard.html";
    }
    
    @GetMapping("/formNewLibro")
    public String showFormNewLibro(Model model) {
        model.addAttribute("libro", new Libro());
        model.addAttribute("autori", this.autoreService.findAll());
        return "admin/formNewLibro.html";
    }
    @GetMapping("/manage/libri")
    public String showManageLibri(Model model) {
        model.addAttribute("libri", this.libroService.getAllLibri());
        return "admin/manageLibri.html";
    }
    
    @GetMapping("/delete/libro/{id}")
    public String deleteLibro(@PathVariable("id") Long id) {
        this.libroService.deleteById(id);
        return "redirect:/admin/manage/libri";
    }
    
    @GetMapping("/formEditLibro/{id}")
    public String showFormEditLibro(@PathVariable("id") Long id, Model model) {
        model.addAttribute("libro", this.libroService.getLibroById(id));
        model.addAttribute("autori", this.autoreService.findAll());
        return "admin/formEditLibro.html";
    }
    
    @PostMapping("/libri/new")
    public String addNewLibro(@Valid @ModelAttribute("libro") Libro libro, BindingResult bindingResult,
                              @RequestParam("autoreId") Long autoreId,
                              @RequestParam("fileImages") MultipartFile[] multipartFiles, Model model) {
        
        if (autoreId != null) {
            libro.setAutore(this.autoreService.findById(autoreId));
        } else {
            bindingResult.rejectValue("autore", "error.autore", "È necessario selezionare un autore.");
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("autori", this.autoreService.findAll());
            return "admin/formNewLibro.html";
        }
        
        try {
            for (MultipartFile file : multipartFiles) {
                if (!file.isEmpty()) {
                    String fileName = StringUtils.cleanPath(file.getOriginalFilename());
                    if (!libro.getNomiImmagini().contains(fileName)) {
                        libro.getNomiImmagini().add(fileName);
                        FileUploadUtil.saveFile("src/main/resources/static/images/uploads/libri", fileName, file);
                    }
                }
            }
        } catch (IOException e) {
            model.addAttribute("uploadError", "Errore durante il salvataggio dell'immagine.");
            model.addAttribute("autori", this.autoreService.findAll());
            return "admin/formNewLibro.html";
        }
        
        this.libroService.save(libro);
        return "redirect:/admin/manage/libri";
    }

    
    @PostMapping("/libri/edit/{id}")
    public String updateLibro(@PathVariable("id") Long id,
                              @Valid @ModelAttribute("libro") Libro libroFromForm, BindingResult bindingResult,
                              @RequestParam("autoreId") Long autoreId,
                              @RequestParam("fileImages") MultipartFile[] multipartFiles, Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("autori", this.autoreService.findAll());
            libroFromForm.setId(id);
            return "admin/formEditLibro.html";
        }
        
        Libro libroDaSalvare = this.libroService.getLibroById(id);
        
        libroDaSalvare.setTitolo(libroFromForm.getTitolo());
        libroDaSalvare.setAnnoPubblicazione(libroFromForm.getAnnoPubblicazione());
        libroDaSalvare.setAutore(this.autoreService.findById(autoreId));
        
        try {
            for (MultipartFile file : multipartFiles) {
                if (!file.isEmpty()) {
                    String fileName = StringUtils.cleanPath(file.getOriginalFilename());
                    if (!libroDaSalvare.getNomiImmagini().contains(fileName)) {
                        libroDaSalvare.getNomiImmagini().add(fileName);
                        FileUploadUtil.saveFile("src/main/resources/static/images/uploads/libri", fileName, file);
                    }
                }
            }
        } catch (IOException e) {
            model.addAttribute("uploadError", "Errore durante il salvataggio dell'immagine.");
            model.addAttribute("autori", this.autoreService.findAll());
            libroFromForm.setId(id);
            return "admin/formEditLibro.html";
        }

        this.libroService.save(libroDaSalvare);
        return "redirect:/admin/manage/libri";
    }

    
    @GetMapping("/manage/autori")
    public String showManageAutori(Model model) {
        model.addAttribute("autori", this.autoreService.findAll());
        return "admin/manageAutori.html";
    }

    @GetMapping("/formNewAutore")
    public String showFormNewAutore(Model model) {
        model.addAttribute("autore", new Autore());
        return "admin/formNewAutore.html";
    }

    @GetMapping("/formEditAutore/{id}")
    public String showFormEditAutore(@PathVariable("id") Long id, Model model) {
        model.addAttribute("autore", this.autoreService.findById(id));
        return "admin/formEditAutore.html";
    }

    @PostMapping("/autori")
    public String saveAutore(@Valid @ModelAttribute("autore") Autore autoreFromForm, BindingResult bindingResult,
                             @RequestParam("fileImage") MultipartFile multipartFile, Model model) {
        if (bindingResult.hasErrors()) {
            return (autoreFromForm.getId() != null) ? "admin/formEditAutore.html" : "admin/formNewAutore.html";
        }

        Autore autoreDaSalvare;
        if (autoreFromForm.getId() != null) { // MODIFICA
            autoreDaSalvare = this.autoreService.findById(autoreFromForm.getId());
            autoreDaSalvare.setNome(autoreFromForm.getNome());
            autoreDaSalvare.setCognome(autoreFromForm.getCognome());
            autoreDaSalvare.setNazionalita(autoreFromForm.getNazionalita());
            if (autoreFromForm.getDataDiNascita() != null) {
                autoreDaSalvare.setDataDiNascita(autoreFromForm.getDataDiNascita());
            }
            autoreDaSalvare.setDataDiMorte(autoreFromForm.getDataDiMorte());
        } else { // CREAZIONE
            autoreDaSalvare = autoreFromForm;
        }

        try {
            if (!multipartFile.isEmpty()) {
                String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
                autoreDaSalvare.setNomeImmagine(fileName);
                FileUploadUtil.saveFile("src/main/resources/static/images/uploads/autori", fileName, multipartFile);
            }
        } catch (IOException e) {
            model.addAttribute("uploadError", "Errore durante il salvataggio dell'immagine.");
            return (autoreFromForm.getId() != null) ? "admin/formEditAutore.html" : "admin/formNewAutore.html";
        }
        
        this.autoreService.save(autoreDaSalvare);
        return "redirect:/admin/manage/autori";
    }

    @GetMapping("/delete/autore/{id}")
    public String deleteAutore(@PathVariable("id") Long id) {
        this.autoreService.deleteById(id);
        return "redirect:/admin/manage/autori";
    }
    
    @GetMapping("/manage/recensioni")
    public String showManageRecensioni(Model model) {
        model.addAttribute("recensioni", this.recensioneService.findAll());
        return "admin/manageRecensioni.html";
    }

    @GetMapping("/delete/recensione/{id}")
    public String deleteRecensione(@PathVariable("id") Long id) {
        this.recensioneService.deleteById(id);
        return "redirect:/admin/manage/recensioni";
    }
    
}