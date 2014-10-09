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

		//Creation d'un objet de type Connection
    	Connection maConnect = null;

    	try{
    		String url = "jdbc:postgresql://172.16.99.2:5432/bdeslaurier";
    		maConnect = DriverManager.getConnection(url, "b.deslaurier", "P@ssword");
    	}catch(Exception e){
    		System.out.println("Une erreur est survenue lors de la connexion a la base de donnee");
    		System.exit(0);
    	}

    	Statement maReq = null;

    	try{
    		maReq = maConnect.createStatement();
    	}catch(Exception e){

    	}

    	String texteRequete = "select * from \"taxi\".\"tarif\"";

		// definition de l'objet qui recuperera le resultat de l'execution de la requete :
		try {
			ResultSet curseurResultat = maReq.executeQuery(texteRequete);

			// tant qu'il y a encore une ligne resultat a lire
			while(curseurResultat.next()){
				maListeAR.add(new AR(curseurResultat.getInt(0), curseurResultat.getDouble(1) ,
						curseurResultat.getDouble(4), curseurResultat.getDouble(7), curseurResultat.getDouble(2)
						, curseurResultat.getDouble(5)));
				maListeAS.add(new AS(curseurResultat.getInt(0), curseurResultat.getDouble(1) ,
						curseurResultat.getDouble(5), curseurResultat.getDouble(7), curseurResultat.getDouble(3)
						, curseurResultat.getDouble(6)));
			 }

			// on ferme le flux resultat
			 curseurResultat.close();

			// on ferme l'objet lie a la connexion
			 maConnect.close();
		} catch (SQLException e) {
		    System.out.println("La requete ne retourne aucun resultat !!!");
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
