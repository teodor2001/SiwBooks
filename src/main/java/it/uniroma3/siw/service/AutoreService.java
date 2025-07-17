package it.uniroma3.siw.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.uniroma3.siw.model.Autore;
import it.uniroma3.siw.repository.AutoreRepository;

@Service
public class AutoreService {

    @Autowired
    private AutoreRepository autoreRepository;

    @Transactional
    public Autore findById(Long id) {
        return autoreRepository.findById(id).orElse(null);
    }

    @Transactional
    public Iterable<Autore> findAll() {
        return autoreRepository.findAll();
    }

    @Transactional
    public void save(Autore autore) {
        autoreRepository.save(autore);
    }
}