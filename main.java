package remboursement;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import remboursement.Calcul;

/**
 * main est le programme principal qui va permettre la création des liste pour aller retour et aller simple, la saisie de l'utilisateur et le calcul du remboursement.
 *
 * @author BaptisteDeslaurier
 * @version 1.0
 */

public class main {

	public static void main(String[] args) {

		// creation d'une liste
		List<AR> maListeAR =  new ArrayList<AR>();
		List<AS> maListeAS =  new ArrayList<AS>();

		/*
		//Chemin d'acces document Winows pc portable
		String backslash= System.getProperty("file.separator") ;
		String monFichierTarif = "C:"+backslash+"Users"+backslash+"maxim_000"+backslash+"Desktop"+backslash+"Cours"+backslash+
								"SLAM 4"+backslash+"Taxi"+backslash+"src"+backslash+"remboursement"+backslash+"docTarif.txt";
		*/

		//Chemin d'acces document Linux pc lycee
		String monFichierTarif = ("/home/etudiant/ProjetBaptiste/remboursement/src/rembv2/docTarif.txt");

		try{
    		//Ouverture d'un flux d'entree à partir du fichier "docTarif.txt"
        	//Pour la version Windows
			BufferedReader monBuffer = new BufferedReader(new FileReader(monFichierTarif));
        	String line = null;					//Variable qui contiendra chaque ligne du fichier

        	//Tant qu'il reste une ligne au fichier
        	while ((line = monBuffer.readLine()) != null)
        	{
        		//On découpe la ligne gràce au caractère ","
        		String[] part = line.split(",");
        		//On ajoute un objet de la classe Tarif à la brochure, à partir de la ligne du fichier découpée
        		maListeAR.add(new AR(Integer.parseInt(part[0]),Double.parseDouble(part[1]),Double.parseDouble(part[4]),Double.parseDouble(part[7]),
        							Double.parseDouble(part[2]),Double.parseDouble(part[5])));
        		maListeAS.add(new AS(Integer.parseInt(part[0]),Double.parseDouble(part[1]),Double.parseDouble(part[5]),Double.parseDouble(part[7]),
						Double.parseDouble(part[3]),Double.parseDouble(part[6])));
        	}
        	//Fermeture du buffer
        	monBuffer.close();
		} catch (Exception e) {
		    System.out.println("Fichier contenant les tarifs introuvable !!!");
		    System.exit(0);
		}

		int i;

		Saisie maSaisie = new Saisie();

		boolean saisieOK = false;

		do{
			boolean trouve = false;
			i = 0;

			while(!trouve && i<maListeAR.size()){
				if(maSaisie.getNumDept()==maListeAR.get(i).getDept()){
					trouve = true;
				}else{
					i++;
				}
			}

			if(trouve){
				saisieOK = true;
			}
			else{
				Scanner deptObjet = new Scanner(System.in);
				System.out.println("Département non trouvé; veuillez resaisir");
				maSaisie.setNumDept(deptObjet.nextInt());
			}
		}while(!saisieOK);

		System.out.println("Le remboursement est de " + Calcul.calculer(i, maListeAR, maListeAS, maSaisie)  + " euros");
	}
}
