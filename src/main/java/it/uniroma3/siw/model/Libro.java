package it.uniroma3.siw.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
public class Libro {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@NotBlank(message="Il libro deve avere un titolo")
	private String titolo;
	
	@NotNull(message="Il libro deve avere un anno di pubblicazione")
	private Integer annoPubblicazione;
	
	@NotBlank(message="Il libro deve avere un'immagine associata")
	private String nomeImmagine;
	
	@NotNull(message="Il libro deve avere un autore")
	@ManyToOne
	private Autore autore;
	
	@OneToMany(mappedBy = "libro", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<Recensione> recensioni = new ArrayList<Recensione>();
	
	//Costruttore vuoto per JPA
	public Libro()
	{
		
	}
	
	public Libro(String titolo, Integer annoPubblicazione, Autore autore, String nomeImmagine) {
		this.titolo = titolo;
		this.annoPubblicazione = annoPubblicazione;
		this.autore = autore;
		this.nomeImmagine = nomeImmagine;
	}
	
	
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		id = id;
	}

	public String getTitolo() {
		return titolo;
	}

	public void setTitolo(String titolo) {
		this.titolo = titolo;
	}

	public Integer getAnnoPubblicazione() {
		return annoPubblicazione;
	}

	public void setAnnoPubblicazione(Integer annoPubblicazione) {
		this.annoPubblicazione = annoPubblicazione;
	}

	public Autore getAutore() {
		return autore;
	}

	public void setAutore(Autore autore) {
		this.autore = autore;
	}

	public String getNomeImmagine() {
		return nomeImmagine;
	}

	public void setNomeImmagine(String nomeImmagine) {
		this.nomeImmagine = nomeImmagine;
	}

	public List<Recensione> getRecensioni() {
		return recensioni;
	}

	public void setRecensioni(List<Recensione> recensioni) {
		this.recensioni = recensioni;
	}

	@Override
	public int hashCode() {
	    return Objects.hash(titolo, annoPubblicazione);
	}

	@Override
	public boolean equals(Object obj) {
	    if (this == obj) return true;
	    if (obj == null || getClass() != obj.getClass()) return false;
	    Libro other = (Libro) obj;
	    return Objects.equals(titolo, other.titolo) && Objects.equals(annoPubblicazione, other.annoPubblicazione);
	}

	@Override
	public String toString() {
		return "Libro [Id=" + id + ", titolo=" + titolo + ", annoPubblicazione=" + annoPubblicazione + ", autore="
				+ autore + ", nomeImmagine=" + nomeImmagine + ", recensioni=" + recensioni + "]";
	}
	
	
	

}
