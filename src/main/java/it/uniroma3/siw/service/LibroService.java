package it.uniroma3.siw.service;

import java.util.List;
import java.util.Set;

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
    
    @Transactional
    public Set<Libro> findLibriByAutore(Autore autore) {
        return this.libroRepository.findByAutoriContains(autore);
    }
    
    @Transactional
    public boolean hasBooks(Autore autore) {
        return this.libroRepository.countByAutoriContains(autore) > 0;
    }
    @Transactional
    public List<Libro> searchByTitolo(String titolo) {
        return libroRepository.findByTitoloContainingIgnoreCase(titolo);
    }

    @Transactional
    public List<Libro> filterByAnnoPubblicazione(Integer anno) {
        return libroRepository.findByAnnoPubblicazione(anno);
    }

    @Transactional
    public List<Libro> searchAndFilterLibri(String keyword, Integer annoPubblicazione) {
        if (keyword != null && !keyword.isEmpty() && annoPubblicazione != null) {
            return libroRepository.searchByKeywordAndAnnoPubblicazione(keyword, annoPubblicazione);
        } else if (keyword != null && !keyword.isEmpty()) {
            return libroRepository.searchByKeyword(keyword);
        } else if (annoPubblicazione != null) {
            return libroRepository.findByAnnoPubblicazione(annoPubblicazione);
        } else {
            return (List<Libro>) libroRepository.findAll();
        }
    }
}