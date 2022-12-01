package it.prova.gestionedottore;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import it.prova.gestionedottore.model.Dottore;
import it.prova.gestionedottore.repository.DottoreRepository;

@SpringBootApplication
public class GestionedottoreApplication implements CommandLineRunner{
	
	@Autowired
	private DottoreRepository repository;

	public static void main(String[] args) {
		SpringApplication.run(GestionedottoreApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		// Popolo DB
		repository.save(new Dottore("Mario","Rossi","MARROSS78P1", "PRTF653DRT",true,false));
		repository.save(new Dottore("Irene","Rossi","IPRTRN642P1", "CFDS5678JH",true,false));
		repository.save(new Dottore("Mario","Bianchi","BVCDS568DS", "NBVDSQA875",false,true));
		
	}

}
