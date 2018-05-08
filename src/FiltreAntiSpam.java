import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class FiltreAntiSpam {

	//stock tous les mots du dictionnaire 
	private String[] dictionnaire;
	//frequence d'apparition du mot i dans les SPAM
	private int[] motsSPAM;
	//frequence d'apparition du mot i dans les HAM
	private int[] motsHAM;
	//nombre de SPAM lu
	private int nbSPAM;
	//nombre de HAM lu
	private int nbHAM;
	//nombre de message lu
	private int nbMessage;
	//parametre pour eviter le surapprentissage
	private int epsylon;
	//probabilite de voir le mot i dans un SPAM
	private double[] bSPAM;
	//probabilite de voir le mot i dans un HAM
	private double[] bHAM;
	//base utilis�e
	private String path;
	
	public static void main(String[] args){
		//initialisation
		int taille = 1000;
		int nbSPAMTest = Integer.parseInt(args[1]);
		int nbHAMTest = Integer.parseInt(args[2]);
		FiltreAntiSpam filtre = new FiltreAntiSpam();
		filtre.path = "baseapp";
		filtre.motsHAM = new int[taille];
		filtre.motsSPAM = new int[taille];
		filtre.bSPAM = new double[taille];
		filtre.bHAM = new double[taille];
		Scanner sc = new Scanner(System.in);
		System.out.println("Combien de SPAM dans la base d'apprentissage ?");
		filtre.nbHAM = sc.nextInt();
		System.out.println("Combien de HAM dans la base d'apprentissage ?");
		filtre.nbSPAM = sc.nextInt();
		filtre.nbMessage = 0;
		filtre.epsylon = 1;
		
		//chargement du dictionnaire
	    filtre.charger_dictionnaire("dictionnaire1000en.txt", taille);
	    System.out.println("Dictionnaire charger.");
	    
	    //lancement de l'apprentissage
	    filtre.apprentissage();
	    
	    //lancement de la phase de test 
	    filtre.path = args[0];
	    //filtre.test(nbSPAMTest, nbHAMTest);
	}
	
	/**
	 * calcul les valeurs de l'ensemble des tableaux bSPAM et bHAM
	 */
	public void calculeFrequenceMot(){
		System.out.println("Calcul des fréquances d'apparition des mots...");
		for(int i = 0;i<this.bHAM.length;i++){
			this.bHAM[i] = (this.motsHAM[i]+this.epsylon)/(this.nbHAM+2*this.epsylon);
			this.bSPAM[i] = (this.motsSPAM[i]+this.epsylon)/(this.nbSPAM+2*this.epsylon);
		}
		System.out.println("Calcul terminé.");
	}
	
	/**
	 * detecte si le message mit en parametre est un spam,sinon c'est un ham
	 * @return
	 * 		vrai si SPAM, faux si HAM
	 */
	public boolean spamDetect(Boolean[] message){
		return true;
	}
	
	/**
	 * lit la base d'apprentissage et met a jour les attributs en fonction de l'apparition des mots
	 */
	public void apprentissage(){
		System.out.println("Apprentissage...");
		Boolean[] message;
		for(int i = 0;i<this.nbHAM;i++){
			message = this.lire_message("ham/"+i+".txt");
			this.update(message, "HAM");
		}
		for(int i = 0;i<this.nbSPAM;i++){
			message = this.lire_message("spam/"+i+".txt");
			this.update(message, "SPAM");
		}
		this.calculeFrequenceMot();
	}
	
	/**
	 * test le classifieur naif sur la base de test
	 * @param nbSPAM
	 * @param nbHAM
	 */
	public void test(int nbSPAM,int nbHAM){
		
		//Boucle sur les Spams
		int nbErreursSpams = 0;
		Boolean [] spam;
		for (int i = 0; i < nbSPAM; i++) {
			spam = this.lire_message(this.path + "/spam/" + i + ".txt");
			Boolean res = this.spamDetect(spam);
			if(res) {
				System.out.println("SPAM num�ro " + i +" identifi� comme un SPAM");
			}else {
				System.out.println("SPAM num�ro " + i +" identifi� comme un HAM *** erreur ***");
				nbErreursSpams++;
			}
		}
		
		//Boucle sur les Hams
		int nbErreursHams = 0;
		Boolean [] ham;
		for (int i = 0; i < nbHAM; i++) {
			ham = this.lire_message(this.path + "/ham/" + i + ".txt");
			Boolean res = this.spamDetect(ham);
			if(!res) {
				System.out.println("HAM num�ro " + i +" identifi� comme un HAM");
			}else {
				System.out.println("HAM num�ro " + i +" identifi� comme un SPAM *** erreur ***");
				nbErreursHams++;
			}
		}
		int PTestErreurSpam = (nbErreursSpams / nbSPAM) * 100;
		int PTestErreurHam = (nbErreursHams / nbHAM) * 100;
		int PTestErreurTotal = ((nbErreursSpams + nbErreursHams) / (nbSPAM + nbSPAM)) * 100;
		System.out.println("Erreur de test sur les " + nbSPAM  + " SPAM : " +PTestErreurSpam + " %");
		System.out.println("Erreur de test sur les " + nbHAM + " HAM : "+ PTestErreurHam + " %");
		System.out.println("Erreur de test globale sur " + (nbSPAM+nbHAM) + " mails : " + PTestErreurTotal + " % ");
		
	}
	
	/**
	 * met a jour les stats d'apparition des mots et le nombre de SPAM/HAM lu
	 * @param message
	 * @param etiquette
	 */
	public void update(Boolean[] message,String etiquette){
		int i = 0;
		for(boolean b:message){
			if(b){
				if(etiquette == "SPAM"){
					this.motsSPAM[i]++;
				}else{
					this.motsHAM[i]++;
				}
			}
			
			i++;
		}
		this.nbMessage++;
	}
	
	/**
	 * charge l'ensemble des mots d'un fichier dans un tableau de string qui represente le dictionnaire
	 * @param nomFichier
	 * @param taille
	 */
	public void charger_dictionnaire(String nomFichier,int taille) { 
	    String line = ""; 
	    BufferedReader br = null; 
	    this.dictionnaire = new String[taille];
	    try { 
	      br = new BufferedReader(new FileReader(nomFichier)); 
	      int i = 0; 
	      // on boucle sur chaque ligne du fichier 
	      while ((line = br.readLine()) != null) { 
	    	  if(line.length()>2){
		        this.dictionnaire[i] = line; 
		        i++; 
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
	  } 
	
	/**
	 * lit un message present dans un fichier et retourne un tableau de boolean qui represente la presence du mot i dans le message
	 * @param nomFichier
	 * @return
	 */
	public Boolean[] lire_message(String nomFichier) { 
	    Boolean[] res = new Boolean[this.dictionnaire.length]; 
	    for (int i = 0; i < res.length; i++) { 
	      res[i] = false; 
	    } 
	    // cette fonction doit pouvoir lire un message (dans un fichier texte) et le 
	    // traduire en une repr�sentation sous forme de vecteur binaire x � partir d
	    // dictionnaire 
	    String line = ""; 
	    BufferedReader br = null; 
	    try { 
	      br = new BufferedReader(new FileReader(this.path+"/"+nomFichier)); 
	      // on boucle sur chaque ligne du fichier 
	      while ((line = br.readLine()) != null) { 
	        for (int i = 0; i < this.dictionnaire.length; i++) { 
	          if (!res[i]) { //on recherche le mot dans le mail seulement si il n'est pas d�j� tro
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
	    } catch (NullPointerException e){
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
