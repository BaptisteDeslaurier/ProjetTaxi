package rembWBP;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JRadioButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.swing.JTextArea;

import com.sun.java.swing.plaf.windows.resources.windows;

import rembWBP.AR;
import rembWBP.AS;
import rembv2.Saisie;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class GUI extends JFrame {

	private JPanel contentPane;
	private JTextField txtDept;
	private JTextField txtTemps;
	private JTextField txtKm;

	/**
	 * Launch the application.
	 */

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GUI frame = new GUI();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public GUI() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel lblDpartement = new JLabel("Département :");
		lblDpartement.setBounds(12, 14, 108, 15);
		contentPane.add(lblDpartement);

		txtDept = new JTextField();
		txtDept.setBounds(121, 12, 114, 19);
		contentPane.add(txtDept);
		txtDept.setColumns(10);

		JLabel lblAsOuAr = new JLabel("AS ou AR :");
		lblAsOuAr.setBounds(12, 43, 108, 15);
		contentPane.add(lblAsOuAr);

		JLabel lblSOuWe = new JLabel("S ou WE :");
		lblSOuWe.setBounds(12, 70, 70, 15);
		contentPane.add(lblSOuWe);

		JLabel lblJOuN = new JLabel("J ou N :");
		lblJOuN.setBounds(12, 97, 70, 15);
		contentPane.add(lblJOuN);

		JLabel lblTemps = new JLabel("Temps :");
		lblTemps.setBounds(12, 124, 70, 15);
		contentPane.add(lblTemps);

		JLabel lblKm = new JLabel("Km :");
		lblKm.setBounds(12, 151, 70, 15);
		contentPane.add(lblKm);

		txtTemps = new JTextField();
		txtTemps.setBounds(78, 122, 114, 19);
		contentPane.add(txtTemps);
		txtTemps.setColumns(10);

		txtKm = new JTextField();
		txtKm.setBounds(50, 151, 114, 19);
		contentPane.add(txtKm);
		txtKm.setColumns(10);

		final JRadioButton rdbtnAs = new JRadioButton("AS");
		rdbtnAs.setBounds(96, 39, 52, 23);
		contentPane.add(rdbtnAs);

		final JRadioButton rdbtnAr = new JRadioButton("AR");
		rdbtnAr.setBounds(152, 39, 149, 23);
		contentPane.add(rdbtnAr);

		final ButtonGroup groupAller = new ButtonGroup();
		groupAller.add(rdbtnAs);
		groupAller.add(rdbtnAr);

		final JRadioButton rdbtnS = new JRadioButton("S");
		rdbtnS.setBounds(96, 66, 41, 23);
		contentPane.add(rdbtnS);

		final JRadioButton rdbtnWe = new JRadioButton("WE");
		rdbtnWe.setBounds(141, 66, 149, 23);
		contentPane.add(rdbtnWe);

		final ButtonGroup groupJour = new ButtonGroup();
		groupJour.add(rdbtnS);
		groupJour.add(rdbtnWe);

		final JRadioButton rdbtnJ = new JRadioButton("J");
		rdbtnJ.setBounds(79, 93, 41, 23);
		contentPane.add(rdbtnJ);

		final JRadioButton rdbtnN = new JRadioButton("N");
		rdbtnN.setBounds(121, 93, 46, 23);
		contentPane.add(rdbtnN);

		final ButtonGroup groupHeure = new ButtonGroup();
		groupHeure.add(rdbtnJ);
		groupHeure.add(rdbtnN);

		final JLabel lblDpartementInconnu = new JLabel("Département inconnu, veuillez le resaisir !!!");
		lblDpartementInconnu.setBounds(12, 180, 404, 15);
		lblDpartementInconnu.setVisible(false);
		contentPane.add(lblDpartementInconnu);

		final JLabel lblPrix = new JLabel("");
		lblPrix.setBounds(12, 203, 424, 15);
		lblPrix.setVisible(false);
		contentPane.add(lblPrix);

		JButton btnAnnuler = new JButton("Annuler");
		btnAnnuler.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				txtDept.setText("");
				groupAller.clearSelection();
				groupJour.clearSelection();
				groupHeure.clearSelection();
				txtTemps.setText("");
				txtKm.setText("");
				lblDpartementInconnu.setVisible(false);
				lblPrix.setVisible(false);
				lblPrix.setText("");
				}
		});
		btnAnnuler.setBounds(155, 230, 117, 25);
		contentPane.add(btnAnnuler);

		JButton btnCalcul = new JButton("Calcul");
		btnCalcul.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			}
		});
		btnCalcul.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {

				java.text.DecimalFormat df = new java.text.DecimalFormat("0.##");

				// crÃ©ation d'une liste
				List<AR> maListeAR =  new ArrayList<AR>();
				List<AS> maListeAS =  new ArrayList<AS>();

				//Remise du lblDpartementInconnu en invisible dans le cas ou il a saisie un département non trouvée auparavant
				lblDpartementInconnu.setVisible(false);

				try{
					Class.forName("org.postgresql.Driver");
				} catch (Exception e1) {
				    System.out.println("Driver PostgreSQL introuvable !!!");
				    System.exit(0);
				}

				//Création d'un objet de type Connection
		    	Connection maConnect = null;

		    	try{
		    		String url = "jdbc:postgresql://172.16.99.2:5432/bdeslaurier";
		    		maConnect = DriverManager.getConnection(url, "b.deslaurier", "passe");
		    	}catch(Exception e2){
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
								curseurResultat.getDouble("tarifHoraireJS"), curseurResultat.getDouble("tarifHoraireWE"), curseurResultat.getDouble("kmASJS")
								, curseurResultat.getDouble("kmASWE")));
					 }

					// on ferme le flux résultat
					curseurResultat.close();

					// on ferme l'objet lié à la connexion
					 maConnect.close();
				} catch (SQLException e3) {
				    System.out.println("La requête ne retourne aucun résultat !!!");
				    System.exit(0);
				}

				int i;
				boolean saisieOK = false;

				do{
					boolean trouve = false;
					i = 0;

					while(!trouve && i<maListeAR.size()){
						if(Integer.parseInt(txtDept.getText())==maListeAR.get(i).getDept()){
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
						txtDept.setText("");
						lblDpartementInconnu.setVisible(true);
					}
				}while(!saisieOK);

				int tps = 0;
				try{
					tps = Integer.parseInt(txtTemps.getText());
				}catch (Exception e2) {
					System.exit(0);
				}

				int km = 0;
				try{
					km = Integer.parseInt(txtKm.getText());
				}catch (Exception e2) {
					System.exit(0);
				}

				lblPrix.setText("Le remboursement est de " + df.format(Calcul.calculer(i, maListeAR, maListeAS, rdbtnAr.isSelected(), rdbtnAs.isSelected(), rdbtnJ.isSelected(), rdbtnN.isSelected(), rdbtnS.isSelected(), rdbtnWe.isSelected(), tps, km)) + " euros");
				lblPrix.setVisible(true);
			}
		});
		btnCalcul.setBounds(12, 230, 117, 25);
		contentPane.add(btnCalcul);

		JLabel lblHeures = new JLabel("Heure(s)");
		lblHeures.setBounds(197, 124, 70, 15);
		contentPane.add(lblHeures);
	}
}
