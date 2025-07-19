package it.uniroma3.siw.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.uniroma3.siw.model.Autore;
import it.uniroma3.siw.model.Libro;
import it.uniroma3.siw.repository.LibroRepository;

@Service
public class LibroService {
	
	@Autowired
	private LibroRepository libroRepository;
	
    @Transactional
	public Libro getLibroById(Long id) {
		return libroRepository.findById(id).orElse(null);
	}
	
    @Transactional
	public Iterable<Libro> getAllLibri() {
		return libroRepository.findAll();
	}
    
    @Transactional
    public void save(Libro libro) {
        libroRepository.save(libro);
    }

    @Transactional
    public void deleteById(Long id) {
        libroRepository.deleteById(id);
    }
}