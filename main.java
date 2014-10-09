package remboursement;
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

		//Déclaration du tableau contenant les departement et de leurs tarifs
		double [][] dept = {{21, 2, 0.86, 1.72, 21.93, 1.29, 2.58, 21.93},
							{25, 2.1, 0.83, 1.66, 22.5, 1.2, 2.4, 22.5},
							{39, 2.1, 0.83, 1.66, 22.5, 1.23, 2.46, 25},
							{44, 2.2, 0.79,	1.58, 24.19, 1.19, 2.37, 24.19},
							{72, 2.15, 0.79, 1.58, 22.86, 1.19,	2.38, 22.86},
							{73, 2.4, 0.84,	1.68, 25.4,	1.26, 2.52,	25.4},
							{74, 3.15, 0.92, 1.84, 17.3, 1.38, 2.76, 17.3},
							{75, 2.5, 1, 1.24, 0, 1.5, 1.5, 0},
							{85, 2.3, 0.8, 1.6, 22.2, 1.2, 2.4,	22.2},
							{90, 2.2, 0.83,1.66, 21, 1.15,	2.3, 21}};

		// creation d'une liste
		List<AR> maListeAR =  new ArrayList<AR>();
		List<AS> maListeAS =  new ArrayList<AS>();

		// ajout d'elements a la liste
		int i;
		for (i=0;i<10;i++){
			// utilisation du contructeur de AR puis AS
			maListeAR.add(new AR((int)dept[i][0], dept[i][1], dept[i][4], dept[i][7],
					dept[i][2], dept[i][5]));
			maListeAS.add(new AS((int)dept[i][0], dept[i][1], dept[i][5], dept[i][7],
					dept[i][3], dept[i][6]));
		}

		Saisie maSaisie = new Saisie();
		boolean saisieOK = false;

		//Faire tant que saisieOK n'est pas a true
		do{
			boolean trouve = false;
			i = 0;

			//Tant que qu'on ne trouve pas et qu'on n'est pas à la fin de la liste maListeAR
			while(!trouve && i<maListeAR.size()){
				//Si le numero de departement saisi est egal à un des numeros des departement dans la liste maListeAR
				if(maSaisie.getNumDept()==maListeAR.get(i).getDept()){
					trouve = true;
				}else{
					i++;
				}
			}

			//Si on a trouve alors saisieOK passe a true
			if(trouve){
				saisieOK = true;
			}
			//Sinon on va lui demander de resaisir le departement
			else{
				Scanner deptObjet = new Scanner(System.in);
				System.out.println("Département non trouvé; veuillez resaisir");
				maSaisie = new Saisie();
				//maSaisie.setNumDept(deptObjet.nextInt());
			}
		}while(!saisieOK);

		System.out.println("Le remboursement est de " + Calcul.calculer(i, maListeAR, maListeAS, maSaisie)  + " euros");
	}
}
