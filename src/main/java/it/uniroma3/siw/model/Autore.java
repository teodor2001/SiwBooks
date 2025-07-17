package it.uniroma3.siw.model;

import java.time.LocalDate;
import java.util.Objects;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
public class Autore {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@NotBlank(message="L'autore deve avere un nome")
	private String nome;
	
	@NotBlank(message="L'autore deve avere un cognome")
	private String cognome;
	
	@NotNull(message="L'autore deve avere una data di nascita")
	private LocalDate dataDiNascita;
	
	private LocalDate dataDiMorte;
	
	@NotBlank(message="L'autore deve avere una nazionalità")
	private String nazionalità;
	
	@NotBlank(message="L'autore deve avere una foto associata")
	private String nomeImmagine;
	
	//Costruttore vuoto per JPA
	public Autore(){
		
	}
	
	public Autore(String nome, String cognome, LocalDate dataDiNascita, LocalDate dataDiMorte, String nazionalità, String nomeImmagine) {
		this.nome = nome;
		this.cognome = cognome;
		this.dataDiNascita = dataDiNascita;
		this.dataDiMorte = dataDiMorte;
		this.nazionalità = nazionalità;
		this.nomeImmagine = nomeImmagine;
	}
	
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getCognome() {
		return cognome;
	}
	public void setCognome(String cognome) {
		this.cognome = cognome;
	}
	public LocalDate getDataDiNascita() {
		return dataDiNascita;
	}
	public void setDataDiNascita(LocalDate dataDiNascita) {
		this.dataDiNascita = dataDiNascita;
	}
	public LocalDate getDataDiMorte() {
		return dataDiMorte;
	}
	public void setDataDiMorte(LocalDate dataDiMorte) {
		this.dataDiMorte = dataDiMorte;
	}
	public String getNazionalità() {
		return nazionalità;
	}
	public void setNazionalità(String nazionalità) {
		this.nazionalità = nazionalità;
	}
	public String getNomeImmagine() {
		return nomeImmagine;
	}
	public void setNomeImmagine(String nomeImmagine) {
		this.nomeImmagine = nomeImmagine;
	}
	@Override
	public int hashCode() {
		return Objects.hash(cognome, dataDiMorte, dataDiNascita, nazionalità, nome, nomeImmagine);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Autore other = (Autore) obj;
		return Objects.equals(cognome, other.cognome) && Objects.equals(dataDiMorte, other.dataDiMorte)
				&& Objects.equals(dataDiNascita, other.dataDiNascita) && Objects.equals(nazionalità, other.nazionalità)
				&& Objects.equals(nome, other.nome) && Objects.equals(nomeImmagine, other.nomeImmagine);
	}
	@Override
	public String toString() {
		return "Autore [nome=" + nome + ", cognome=" + cognome + ", dataDiNascita=" + dataDiNascita + ", dataDiMorte="
				+ dataDiMorte + ", nazionalità=" + nazionalità + ", nomeImmagine=" + nomeImmagine + "]";
	}

}
