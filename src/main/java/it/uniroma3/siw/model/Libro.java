package it.uniroma3.siw.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

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
	private Set<String> nomiImmagini = new HashSet<>();
	
	@ManyToMany
	private Set<Autore> autori = new HashSet<>();
	
	@OneToMany(mappedBy = "libro", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private Set<Recensione> recensioni = new HashSet<>();
	
	//Costruttore vuoto per JPA
	public Libro()
	{
		
	}
	
	public Libro(String titolo, Integer annoPubblicazione, Autore autore) {
		this.titolo = titolo;
		this.annoPubblicazione = annoPubblicazione;
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

	public Integer getAnnoPubblicazione() {
		return annoPubblicazione;
	}

	public void setAnnoPubblicazione(Integer annoPubblicazione) {
		this.annoPubblicazione = annoPubblicazione;
	}

	public Set<Autore> getAutori() {
		return autori;
	}

	public void setAutori(Set<Autore> autori) {
		this.autori = autori;
	}

	public Set<String> getNomiImmagini() {
		return nomiImmagini;
	}
	public void setNomiImmagini(Set<String> nomiImmagini) {
		this.nomiImmagini = nomiImmagini;
	}

	public Set<Recensione> getRecensioni() {
		return recensioni;
	}
	public void setRecensioni(Set<Recensione> recensioni) {
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
		return "Libro [id=" + id + ", titolo=" + titolo + ", annoPubblicazione=" + annoPubblicazione + ", nomiImmagini="
				+ nomiImmagini + ", autori=" + autori + ", recensioni=" + recensioni + "]";
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
            return "placeholder.png";
        }
        return "uploads/libri/" + this.nomiImmagini.iterator().next();
    }
    
    public String getAutoriConcatenati() {
        if (autori == null || autori.isEmpty()) return "Nessun autore";
        StringBuilder sb = new StringBuilder();
        List<Autore> autoriList = new ArrayList<>(autori);
        for (int i = 0; i < autoriList.size(); i++) {
            Autore autore = autoriList.get(i);
            sb.append(autore.getNome()).append(" ").append(autore.getCognome());
            if (i < autoriList.size() - 1) sb.append(", ");
        }
        return sb.toString();
    }

}
