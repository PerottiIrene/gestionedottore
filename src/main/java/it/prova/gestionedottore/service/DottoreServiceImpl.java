package it.prova.gestionedottore.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import it.prova.gestionedottore.model.Dottore;
import it.prova.gestionedottore.repository.DottoreRepository;

public class DottoreServiceImpl implements DottoreService{
	
	@Autowired
	private DottoreRepository dottoreRepository;

	@Override
	public Dottore findByCodiceDipendente(String cfInput) {
		return dottoreRepository.findByCodiceDottore(cfInput);
	}

	@Override
	public Dottore inserisciNuovo(Dottore transientInstance) {
		return dottoreRepository.save(transientInstance);
	}

	@Override
	public List<Dottore> listAll() {
		return (List<Dottore>) dottoreRepository.findAll();
	}

	@Override
	public Dottore aggiorna(Dottore transientInstance) {
		return dottoreRepository.save(transientInstance);
	}

	@Override
	public Dottore caricaSingoloElemento(Long id) {
		return dottoreRepository.findById(id).orElse(null);
	}

	@Override
	public void rimuovi(Long id) {
		dottoreRepository.deleteById(id);
	}

}
