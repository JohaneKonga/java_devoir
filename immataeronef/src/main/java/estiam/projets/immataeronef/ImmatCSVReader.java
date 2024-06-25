// Cette ligne déclare que la classe ImmatCSVReader fait partie du package estiam.projets.immataeronef
package estiam.projets.immataeronef;

/*  imports statiques pour des méthodes spécifiques ainsi 
 * que l'utilisation de différentes classes provenant des bibliothèques 
 * standard de Java et de la bibliothèque OpenCSV..*/
import static java.util.Arrays.asList;
import static java.util.Collections.unmodifiableMap;
import static java.util.Objects.requireNonNull;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvException;

// Cette ligne déclare une classe publique ImmatCSVReader
public class ImmatCSVReader {

	/* Cette ligne déclare un champ privé et final entries, qui est une Map où la clé est une String
	représentant l'immatriculation, et la valeur est une autre Map représentant les colonnes et 
	valeurs correspondantes des enregistrements CSV.*/
	private final Map<String, Map<String, String>> entries;

	// Ce constructeur initialise le champ entries avec une nouvelle instance de HashMap
	public ImmatCSVReader() {
		entries = new HashMap<>();
	}

	/*Cette méthode publique importFile prend un objet File en paramètre et lève des exceptions 
IOException et CsvException.
*/
	public void importFile(File export) throws IOException, CsvException {

		/*Ces lignes créent un FileReader pour lire le fichier CSV. La déclaration et l'initialisation sont 
		séparées pour permettre la fermeture du FileReader dans le bloc finally.*/		
		FileReader filereader = null;
		try {
			filereader = new FileReader(export);

			/*Ces lignes créent un CSVParser en utilisant le CSVParserBuilder, spécifiant que les champs 
			sont séparés par un point-virgule et les valeurs sont entourées par des backticks*/
			final var parser = new CSVParserBuilder()
					.withSeparator(';')
					.withQuoteChar('`');

			/*Ces lignes créent un CSVReader en utilisant le CSVReaderBuilder et le CSVParser configuré 
			précédemment.*/

			final var csvReader = new CSVReaderBuilder(filereader)
					.withCSVParser(parser.build())
					.build();
			/*Ces lignes déclarent un tableau de String pour stocker les enregistrements CSV lus et une liste 
			header pour stocker les noms de colonnes.*/
			String[] nextRecord;
			var header = new ArrayList<String>(); 
			/*lecture de  chaque enregistrement du fichier CSV :
				• Si le header est vide, la première ligne est ajoutée à la liste header.
				• Pour chaque ligne suivante, un mapRow est créé pour stocker les colonnes et valeurs.
				• mapRow est ajouté à entries avec la première colonne comme clé.*/

			while ((nextRecord = csvReader.readNext()) != null) {
				if (header.isEmpty()) {
					header.addAll(asList(nextRecord));
					continue;
				}
				
				var mapRow = new HashMap<String, String>();
				for (int pos = 0; pos < nextRecord.length; pos++) {
					mapRow.put(header.get(pos), nextRecord[pos]);
				}
				
				entries.put(nextRecord[0], mapRow);
			}
		} /*Le bloc finally garantit que le FileReader est fermé, même si une exception est levée.*/
		finally {
			if (filereader != null) {
				filereader.close();
			}
		}
	}
	/*Cette méthode publique getEntryByImmat retourne une vue non modifiable de l'entrée 
	correspondant à l'immatriculation donnée. Si l'immatriculation est absente, elle retourne une 
	Map vide*/
	public Map<String, String> getEntryByImmat(String immat) {
		return unmodifiableMap(entries.getOrDefault(requireNonNull(immat), Map.of()));
	}

}
