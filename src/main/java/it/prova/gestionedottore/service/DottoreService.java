package it.prova.gestionedottore.service;

import java.util.List;

import it.prova.gestionedottore.model.Dottore;


public interface DottoreService {
	
	public Dottore findByCodiceDipendente(String cfInput);

	public Dottore inserisciNuovo(Dottore transientInstance);

	public List<Dottore> listAll();
	
	public Dottore aggiorna(Dottore transientInstance);
	
	public Dottore caricaSingoloElemento(Long id);
	
	public void rimuovi(Long id);
	

}
