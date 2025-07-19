package it.uniroma3.siw.repository;

import java.util.Set;

import org.springframework.data.repository.CrudRepository;

import it.uniroma3.siw.model.Autore;
import it.uniroma3.siw.model.Libro;

public interface LibroRepository extends CrudRepository<Libro, Long> {
	
	public Set<Libro> findByAutoriContains(Autore autore);
	public int countByAutoriContains(Autore autore);

}