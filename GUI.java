package rembWBP;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JRadioButton;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import rembWBP.AR;
import rembWBP.AS;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * GUI est l'interface graphique du programme.
 *
 * @author BaptisteDeslaurier
 * @version 1.0
 */

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

	// création des listes
	List<AR> maListeAR =  new ArrayList<AR>();
	List<AS> maListeAS =  new ArrayList<AS>();
	
	/**
	 * Create the frame.
	 */
	public GUI() {
		//Code exécuté a l'ouverture de l'application
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowOpened(WindowEvent e) {
				try{
					Class.forName("org.postgresql.Driver");
				} catch (Exception e1) {
					JOptionPane jopDriver = new JOptionPane();
					jopDriver.showMessageDialog(null, "Driver PostgreSQL introuvable !!!", "Erreur", JOptionPane.ERROR_MESSAGE);
				}

				//Création d'un objet de type Connection
		    	Connection maConnect = null;

		    	try{
		    		// URL lycée interne
		    		//String url = "jdbc:postgresql://172.16.99.2:5432/bdeslaurier";
		    		// URL lycée externe
		    		String url = "jdbc:postgresql://bts.bts-malraux72.net:62543/bdeslaurier";
		    		maConnect = DriverManager.getConnection(url, "b.deslaurier", "passe");
		    	}catch(Exception e2){
		    		JOptionPane jopBDD = new JOptionPane();
		    		jopBDD.showMessageDialog(null, "Connection à la base de donnée impossible !!!", "Erreur", JOptionPane.ERROR_MESSAGE);
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
					//Boîte du message d'erreur
					JOptionPane jopReqSQL = new JOptionPane();
					jopReqSQL.showMessageDialog(null, "Aucune donnée !!!", "Erreur", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
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
		txtDept.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				int lastCharIndex = txtDept.getText().length();

				if (lastCharIndex != 0) {
					txtDept.setEnabled(true);
					String oldString = txtDept.getText();
					char lastChar = txtDept.getText().charAt(lastCharIndex - 1);
					if (Character.isDigit(lastChar) == true){
						txtDept.setText(oldString);
					}else{
						txtDept.setText(oldString.substring(0, lastCharIndex - 1));
					}

					if (txtDept.getText().length() > 2){
						txtDept.setText(oldString.substring(0, lastCharIndex - 1));
					}
				}
			}
		});
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
		txtTemps.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				int lastCharIndex = txtTemps.getText().length();

				if (lastCharIndex != 0) {
					txtTemps.setEnabled(true);
					String oldString = txtTemps.getText();
					char lastChar = txtTemps.getText().charAt(lastCharIndex - 1);
					if (Character.isDigit(lastChar) == true){
						txtTemps.setText(oldString);
					}else{
						txtTemps.setText(oldString.substring(0, lastCharIndex - 1));
					}

					if (txtTemps.getText().length() > 2){
						txtTemps.setText(oldString.substring(0, lastCharIndex - 1));
					}
				}
			}
		});
		txtTemps.setBounds(78, 122, 114, 19);
		contentPane.add(txtTemps);
		txtTemps.setColumns(10);

		txtKm = new JTextField();
		txtKm.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				int lastCharIndex = txtKm.getText().length();

				if (lastCharIndex != 0) {
					txtKm.setEnabled(true);
					String oldString = txtKm.getText();
					char lastChar = txtKm.getText().charAt(lastCharIndex - 1);
					if (Character.isDigit(lastChar) == true){
						txtKm.setText(oldString);
					}else{
						txtKm.setText(oldString.substring(0, lastCharIndex - 1));
					}

					if (txtKm.getText().length() > 2){
						txtKm.setText(oldString.substring(0, lastCharIndex - 1));
					}
				}
			}
		});
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
		btnCalcul.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {

				java.text.DecimalFormat df = new java.text.DecimalFormat("0.##");

				//Remise du lblDpartementInconnu en invisible dans le cas ou il a saisie un département non trouvée auparavant
				lblDpartementInconnu.setVisible(false);
				//Remise du lblPrix en invisible dans le cas ou l'utilisateur veut refaire un calcul et qu'il met un département inconnu
				lblPrix.setVisible(false);
				
				int i;
				boolean saisieOK = false;

				//Faire tant que la saisie du département n'est pas trouvé dans la liste
				do{
					boolean trouve = false;
					i = 0;

					//Tant qu'on n'as pas trouvé et pas fini la liste
					while(!trouve && i<maListeAR.size()){
						if(Integer.parseInt(txtDept.getText())==maListeAR.get(i).getDept()){
							trouve = true;
						}else{
							i++;
						}
					}

					//Si on a trouvé on va passé la saisieOK à vrai sinon on affichera un message pour prévenir que le département n'existe pas
					if(trouve){
						saisieOK = true;
					}
					else{
						txtDept.setText("");
						lblDpartementInconnu.setVisible(true);
					}
				}while(!saisieOK);

				int tps = 0;
				//Essaye de passer le temps saisie en entier si cela ni arrive pas on affichera une boite de dialogue le prévenant sur sa mauvaise saisie
				try{
					tps = Integer.parseInt(txtTemps.getText());
				}catch (Exception e2) {
					JOptionPane jopConvHeure = new JOptionPane();
					jopConvHeure.showMessageDialog(null, "L'heure saisie n'est pas valide (entier) !!!", "Erreur", JOptionPane.ERROR_MESSAGE);
				}

				int km = 0;
				//Essaye de passer les km saisie en entier si cela ni arrive pas on affichera une boite de dialogue le prévenant sur sa mauvaise saisie
				try{
					km = Integer.parseInt(txtKm.getText());
				}catch (Exception e2) {
					JOptionPane jopConvKm = new JOptionPane();
					jopConvKm.showMessageDialog(null, "Le kilométrage saisi n'est pas valide (entier) !!!", "Erreur", JOptionPane.ERROR_MESSAGE);
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
