package it.uniroma3.siw.repository;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import it.uniroma3.siw.model.Autore;
import it.uniroma3.siw.model.Libro;

public interface LibroRepository extends CrudRepository<Libro, Long> {
	
	public Set<Libro> findByAutoriContains(Autore autore);
	public int countByAutoriContains(Autore autore);
	public List<Libro> findByTitoloContainingIgnoreCase(String titolo);

    public List<Libro> findByTitoloContainingIgnoreCaseAndAnnoPubblicazione(String titolo, Integer annoPubblicazione);

    public List<Libro> findByAnnoPubblicazione(Integer annoPubblicazione);
    
    @Query("SELECT DISTINCT l FROM Libro l JOIN l.autori a WHERE LOWER(l.titolo) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(a.nome) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(a.cognome) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    public List<Libro> searchByKeyword(@Param("keyword") String keyword);

    @Query("SELECT DISTINCT l FROM Libro l JOIN l.autori a " +
           "WHERE (LOWER(l.titolo) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
           "OR LOWER(a.nome) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
           "OR LOWER(a.cognome) LIKE LOWER(CONCAT('%', :keyword, '%'))) " +
           "AND l.annoPubblicazione = :annoPubblicazione")
    public List<Libro> searchByKeywordAndAnnoPubblicazione(@Param("keyword") String keyword, @Param("annoPubblicazione") Integer annoPubblicazione);


}