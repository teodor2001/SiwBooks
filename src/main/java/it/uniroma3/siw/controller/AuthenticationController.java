package it.uniroma3.siw.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import it.uniroma3.siw.model.Credentials;
import it.uniroma3.siw.model.User;
import it.uniroma3.siw.service.CredentialsService;
import jakarta.validation.Valid;

@Controller
public class AuthenticationController {

    @Autowired
    private CredentialsService credentialsService;

    @GetMapping("/login")
    public String showLoginForm() {
        return "formLogin.html";
    }

    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("credentials", new Credentials());
        return "formRegister.html";
    }

    @PostMapping("/register")
    public String registerUser(@Valid @ModelAttribute("user") User user,
                               BindingResult userBindingResult,
                               @Valid @ModelAttribute("credentials") Credentials credentials,
                               BindingResult credentialsBindingResult,
                               Model model) {

        if (credentialsService.existsByUsername(credentials.getUsername())) {
            model.addAttribute("usernameError", "Questo username è già in uso");
            return "formRegister.html";
        }
        
        if (userBindingResult.hasErrors() || credentialsBindingResult.hasErrors()) {
            return "formRegister.html";
        }
        
        credentials.setUser(user);
        credentialsService.saveCredentials(credentials);
        
        return "redirect:/login?registration_success";
    }
}