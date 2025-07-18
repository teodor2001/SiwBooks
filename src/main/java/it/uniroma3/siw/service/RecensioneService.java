package it.uniroma3.siw.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.uniroma3.siw.model.Libro;
import it.uniroma3.siw.model.Recensione;
import it.uniroma3.siw.model.User;
import it.uniroma3.siw.repository.RecensioneRepository;

@Service
public class RecensioneService {

    @Autowired
    private RecensioneRepository recensioneRepository;

    @Transactional
    public void save(Recensione recensione) {
        recensioneRepository.save(recensione);
    }
    
    @Transactional
    public Iterable<Recensione> findAll() {
        return recensioneRepository.findAll();
    }
    
    @Transactional
    public void deleteById(Long id) {
        recensioneRepository.deleteById(id);
    }
    
    @Transactional
    public boolean hasUserReviewedBook(Libro libro, User utente) {
        return this.recensioneRepository.existsByLibroAndUtente(libro, utente);
    }
}