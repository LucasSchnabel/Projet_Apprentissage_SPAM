import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class filtreAntiSpam {

	private String[] dictionnaire;
	
	public static void main(String[] args){
		
	}
	
	public void charger_dictionnaire(String nomFichier) { 
	    String line = ""; 
	    BufferedReader br = null; 
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
	
	public boolean[] lire_message(String nomFichier){
		boolean[] res = null;
		//cette fonction doit pouvoir lire un message (dans un fichier texte) et le
		//traduire en une représentation sous forme de vecteur binaire x à partir d’un dictionnaire
		return res;
	}
}
