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
	
	@ElementCollection
	@CollectionTable(name = "libro_immagini", joinColumns = @JoinColumn(name = "libro_id"))
	@Column(name = "nome_immagine")
	private List<String> nomiImmagini = new ArrayList<>();
	
	@ManyToOne
	private Autore autore;
	
	@OneToMany(mappedBy = "libro", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<Recensione> recensioni = new ArrayList<Recensione>();
	
	//Costruttore vuoto per JPA
	public Libro()
	{
		
	}
	
	public Libro(String titolo, Integer annoPubblicazione, Autore autore) {
		this.titolo = titolo;
		this.annoPubblicazione = annoPubblicazione;
		this.autore = autore;
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

	public List<String> getNomiImmagini() {
		return nomiImmagini;
	}

	public void setNomiImmagini(List<String> nomiImmagini) {
		this.nomiImmagini = nomiImmagini;
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
				+ autore + ", nomiImmagini=" + nomiImmagini + ", recensioni=" + recensioni + "]";
	}
	
    public double getAverageRating() {
        if (recensioni == null || recensioni.isEmpty()) {
            return 0.0;
        }
        double sum = 0;
        for (Recensione r : recensioni) {
            sum += r.getVoto();
        }
        return sum / recensioni.size();
    }
    
    public int getNumberOfReviews() {
        return (recensioni != null) ? recensioni.size() : 0;
    }
    public String getCopertina() {
        if (this.nomiImmagini == null || this.nomiImmagini.isEmpty()) {
            return "placeholder.png"; // Ritorna un placeholder se non ci sono immagini
        }
        return "uploads/libri/" + this.nomiImmagini.get(0);
    }
	

}
