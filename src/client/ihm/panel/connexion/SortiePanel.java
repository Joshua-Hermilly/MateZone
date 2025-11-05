package client.ihm.panel.connexion;

import javax.swing.*;
import java.awt.*;

/**
 * Panel réutilisable qui contient une JTextArea en scroll pour afficher la sortie/log.
 */
public class SortiePanel extends JPanel 
{
	/*--------------------------*/
	/*  Composants              */
	/*--------------------------*/
    private JTextArea txtOutput;

	/*--------------------------*/
	/*     Constructeur         */
	/*--------------------------*/
    public SortiePanel() 
	{
        this.setLayout( new BorderLayout() );

		/*-------------------------------*/
		/* Création des composants       */
		/*-------------------------------*/
        this.txtOutput = new JTextArea();
        this.txtOutput.setEditable(false);

		/*-------------------------------*/
		/* Ajout des composants          */
		/*-------------------------------*/
        this.add( new JScrollPane(this.txtOutput), BorderLayout.CENTER );
    }

	/*--------------------------*/
	/*  Méthodes                */
	/*--------------------------*/
    public void append(String text) 
	{
        this.txtOutput.append(text);
    }
}
