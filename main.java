package remboursement;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import remboursement.Calcul;

/**
 * main est le programme principal qui va permettre la crÃ©ation des liste pour aller retour et aller simple, la saisie de l'utilisateur et le calcul du remboursement.
 *
 * @author BaptisteDeslaurier
 * @version 1.0
 */

public class main {

	public static void main(String[] args) {

		// creation d'une liste
		List<AR> maListeAR =  new ArrayList<AR>();
		List<AS> maListeAS =  new ArrayList<AS>();

		try{
			Class.forName("org.postgresql.Driver");
		} catch (Exception e) {
		    System.out.println("Driver PostgreSQL introuvable !!!");
		    System.exit(0);
		}

		//Création d'un objet de type Connection
    	Connection maConnect = null;

    	try{
    		String url = "jdbc:postgresql://172.16.99.2:5432/bdeslaurier";
    		maConnect = DriverManager.getConnection(url, "b.deslaurier", "P@ssword");
    	}catch(Exception e){
    		System.out.println("Une erreur est survenue lors de la connexion à la base de donnée");
    		System.exit(0);
    	}

    	String texteRequete = "select * from \"taxi\".\"tarif\"";

		// définition de l'objet qui récupérera le résultat de l'exécution de la requête :
		ResultSet curseurResultat = null;
		try {
			Statement maReq = maConnect.createStatement();
			curseurResultat = maReq.executeQuery(texteRequete);

			// Récupération des détails du résultats
	         ResultSetMetaData detailsDonnees = curseurResultat.getMetaData();

			// tant qu'il y a encore une ligne résultat à lire
			while(curseurResultat.next()){
				maListeAR.add(new AR(curseurResultat.getInt("departement"), curseurResultat.getDouble("prisEnCharge") ,
						curseurResultat.getDouble("tarifHoraireJS"), curseurResultat.getDouble("tarifHoraireWE"), curseurResultat.getDouble("kmARJS")
						, curseurResultat.getDouble("kmARWE")));
				maListeAS.add(new AS(curseurResultat.getInt("departement"), curseurResultat.getDouble("prisEnCharge") ,
						curseurResultat.getDouble(5), curseurResultat.getDouble("tarifHoraireWE"), curseurResultat.getDouble("kmASJS")
						, curseurResultat.getDouble("kmASWE")));
			 }

			// on ferme le flux résultat
			// curseurResultat.close();

			// on ferme l'objet lié à la connexion
			 maConnect.close();
		} catch (SQLException e) {
		    System.out.println("La requête ne retourne aucun résultat !!!");
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
				System.out.println("Departement non trouve; veuillez resaisir");
				maSaisie.setNumDept(deptObjet.nextInt());
			}
		}while(!saisieOK);

		System.out.println("Le remboursement est de " + Calcul.calculer(i, maListeAR, maListeAS, maSaisie)  + " euros");

	}

}
