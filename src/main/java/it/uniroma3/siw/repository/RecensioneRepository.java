package it.uniroma3.siw.repository;

import org.springframework.data.repository.CrudRepository;
import it.uniroma3.siw.model.Libro;
import it.uniroma3.siw.model.Recensione;
import it.uniroma3.siw.model.User;

public interface RecensioneRepository extends CrudRepository<Recensione, Long> {
	
	//Controllo se un determinato utente ha già lasciato una recensione a quel determinato libro. Utile per le direttive del progetto
    boolean existsByLibroAndUtente(Libro libro, User utente);
}