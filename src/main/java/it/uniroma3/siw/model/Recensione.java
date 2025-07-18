package it.uniroma3.siw.model;
import java.util.Objects;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Entity
public class Recensione {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@NotBlank(message = "La recensione deve avere un titolo")
	public String titolo;
	
    @NotNull(message = "Il voto non può essere nullo")
	@Min(value = 1, message = "Il voto deve essere almeno 1")
	@Max(value = 5, message = "Il voto non può essere superiore a 5")
	public Integer voto;
    
    @NotBlank(message = "Il testo non può essere vuoto")
	@Column(columnDefinition = "TEXT")
	public String testo;
    
	@ManyToOne
	private Libro libro;
    
	@ManyToOne
	private User utente;
    
    //Costruttore vuoto per JPA
    public Recensione()
    {
    	
    }
	
	
	public Recensione(String titolo, Integer voto, String testo, Libro libro, User utente) {
		this.titolo = titolo;
		this.voto = voto;
		this.testo = testo;
		this.libro = libro;
		this.utente = utente;
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getTitolo() {
		return titolo;
	}
	public void setTitolo(String titolo) {
		this.titolo = titolo;
	}
	public Integer getVoto() {
		return voto;
	}
	public void setVoto(Integer voto) {
		this.voto = voto;
	}
	public String getTesto() {
		return testo;
	}
	public void setTesto(String testo) {
		this.testo = testo;
	}
	public Libro getLibro() {
		return libro;
	}
	public void setLibro(Libro libro) {
		this.libro = libro;
	}
	public User getUtente() {
		return utente;
	}
	public void setUtente(User utente) {
		this.utente = utente;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, libro, testo, titolo, utente, voto);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Recensione other = (Recensione) obj;
		return Objects.equals(id, other.id) && Objects.equals(libro, other.libro) && Objects.equals(testo, other.testo)
				&& Objects.equals(titolo, other.titolo) && Objects.equals(utente, other.utente)
				&& Objects.equals(voto, other.voto);
	}

	@Override
	public String toString() {
		return "Recensione [id=" + id + ", titolo=" + titolo + ", voto=" + voto + ", testo=" + testo + ", libro="
				+ libro + ", utente=" + utente + "]";
	}
	
	
	
	

}
