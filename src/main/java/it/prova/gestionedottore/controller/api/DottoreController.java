package it.prova.gestionedottore.controller.api;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import it.prova.gestionedottore.dto.DottoreDTO;
import it.prova.gestionedottore.model.Dottore;
import it.prova.gestionedottore.service.DottoreService;

@RestController
@RequestMapping("/api/dottore")
public class DottoreController {

	@Autowired
	private DottoreService dottoreService;

	@GetMapping("/{id}")
	public DottoreDTO findById(@PathVariable(value = "cf", required = true) Long id) {
		Dottore result = dottoreService.caricaSingoloElemento(id);
		// qui andrebbe gestito con un 404 ma per semplicità mandiamo un oggetto vuoto
		return result == null ? new DottoreDTO() : DottoreDTO.buildDottoreDTOFromModel(result);
	}

	@GetMapping
	public List<DottoreDTO> getAll() {
		return DottoreDTO.createFilmDTOListFromModelList(dottoreService.listAll());
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public DottoreDTO createNew(@RequestBody DottoreDTO input) {
		// ANDREBBE GESTITA CON ADVICE!!!
		// se mi viene inviato un id jpa lo interpreta come update ed a me (producer)
		// non sta bene
		if (input.getId() != null)
			throw new RuntimeException("Non è ammesso fornire un id per la creazione");

		Dottore result = dottoreService.inserisciNuovo(input.buildDottoreModel());
		return DottoreDTO.buildDottoreDTOFromModel(result);
	}

	@PutMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public DottoreDTO update(@Valid @RequestBody DottoreDTO input, @PathVariable(required = true) Long id) {

		Dottore dottore = dottoreService.caricaSingoloElemento(id);
		// ANDREBBE GESTITA CON ADVICE!!!
		// se mi viene inviato un id jpa lo interpreta come update ed a me (producer)
		// non sta bene
		if (dottore.getId() == null)
			throw new RuntimeException("Dottore not found con id: " + id);

		input.setId(id);
		Dottore dottoreAggiornato = dottoreService.aggiorna(input.buildDottoreModel());
		return DottoreDTO.buildDottoreDTOFromModel(dottoreAggiornato);
	}

	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void delete(@PathVariable(required = true) Long id) {

		Dottore dottore = dottoreService.caricaSingoloElemento(id);

		if (dottore.getId() == null)
			throw new RuntimeException("Dottore not found con id: " + id);

		if (dottore.isInServizio())
			throw new RuntimeException("Impossibile eliminare il dottore, e' in servizio");

		if (dottore.isInVisita())
			throw new RuntimeException("Impossibile eliminare il dottore, e' in visita");

		dottoreService.rimuovi(id);
	}

	@GetMapping("/verificaDisponibilita/{codiceDottore}")
	public DottoreDTO verificaDisponibilita(
			@PathVariable(value = "codiceDottore", required = true) String codiceDottore) {

		Dottore result = dottoreService.findByCodiceDipendente(codiceDottore);

		if (result.isInVisita()) {
			throw new RuntimeException("dottore non disponible, in visita");
		}

		return DottoreDTO.buildDottoreDTOFromModel(result);
	}

	@PostMapping("/impostaInVisita")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public DottoreDTO impostaInVisita(@Valid @RequestBody DottoreDTO input) {

		Dottore result = dottoreService.findByCodiceDipendente(input.getCodiceDottore());
		// ANDREBBE GESTITA CON ADVICE!!!
		// se mi viene inviato un id jpa lo interpreta come update ed a me (producer)
		// non sta bene
		if (result.getId() == null)
			throw new RuntimeException("Dottore not found ");

		result.setInVisita(true);
		result.setCodiceFiscalePazienteAttualmenteInVisita(input.getCodiceFiscalePazienteAttualmenteInVisita());

		dottoreService.aggiorna(result);

		return DottoreDTO.buildDottoreDTOFromModel(result);
	}

	@GetMapping("/terminaVisita/{codiceDottore}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public DottoreDTO terminaVisita(
			@Valid @PathVariable(value = "codiceDottore", required = true) String codiceDottore) {

		Dottore result = dottoreService.findByCodiceDipendente(codiceDottore);
		// ANDREBBE GESTITA CON ADVICE!!!
		// se mi viene inviato un id jpa lo interpreta come update ed a me (producer)
		// non sta bene
		if (result.getId() == null)
			throw new RuntimeException("Dottore not found ");

		result.setInVisita(false);
		result.setCodiceFiscalePazienteAttualmenteInVisita(null);

		dottoreService.aggiorna(result);

		return DottoreDTO.buildDottoreDTOFromModel(result);
	}

	@PostMapping("/ricovera")
	public DottoreDTO ricovera(@Valid @RequestBody DottoreDTO dottore) {
		Dottore dottoreReloaded = dottoreService.findByCodiceDipendente(dottore.getCodiceDottore());

		dottoreReloaded.setCodiceFiscalePazienteAttualmenteInVisita(null);
		dottoreReloaded.setInVisita(false);
		dottoreService.aggiorna(dottoreReloaded);
		return dottore;

	}

}
