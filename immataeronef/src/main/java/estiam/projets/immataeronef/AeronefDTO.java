package estiam.projets.immataeronef;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AeronefDTO {

	private String immatriculation; 
	private String constructeur;
	private String modele;
	private String aerodromeAttache;
	//on declare la variable privée proprietaire
	private String proprietaire;
	
}
