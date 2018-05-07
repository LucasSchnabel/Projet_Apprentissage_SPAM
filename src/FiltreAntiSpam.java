import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class FiltreAntiSpam {

	private String[] dictionnaire;

	public static void main(String[] args) {
		FiltreAntiSpam filtre = new FiltreAntiSpam();
		filtre.charger_dictionnaire("dictionnaire1000en.txt", 1000);
		Boolean [] ham = filtre.lire_message("baseapp/ham/0.txt");
		int i = 0;
		for(Boolean b : ham) {
			if(b) i++;
		}
		System.out.println("nb mots ham : "+i);
		i = 0;
		Boolean [] spam = filtre.lire_message("baseapp/spam/0.txt");
		for(Boolean b : spam) {
			if(b) i++;
		}
		System.out.println("nb mots spam : "+i);
	}

	/**
	 * 
	 * @param nomFichier chemin du fichier
	 * @param taille entier définissant la taille du dictionnaire
	 */
	public void charger_dictionnaire(String nomFichier, int taille) {
		String line = "";
		BufferedReader br = null;
		this.dictionnaire = new String[taille];
		try {
			br = new BufferedReader(new FileReader(nomFichier));
			int i = 0;
			// on boucle sur chaque ligne du fichier
			while ((line = br.readLine()) != null) {
				this.dictionnaire[i] = line;
				i++;
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public Boolean[] lire_message(String nomFichier) {
		Boolean[] res = new Boolean[this.dictionnaire.length];
		for (int i = 0; i < res.length; i++) {
			res[i] = false;
		}
		// cette fonction doit pouvoir lire un message (dans un fichier texte) et le
		// traduire en une représentation sous forme de vecteur binaire x à partir d’un
		// dictionnaire
		String line = "";
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(nomFichier));
			// on boucle sur chaque ligne du fichier
			while ((line = br.readLine()) != null) {
				for (int i = 0; i < this.dictionnaire.length; i++) {
					if (!res[i]) { //on recherche le mot dans le mail seulement si il n'est pas déjà trouvé
						if (line.contains(this.dictionnaire[i])) {
							res[i] = true;
						}
					}
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return res;
	}
}
