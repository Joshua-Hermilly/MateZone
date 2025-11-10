package client.ihm;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

import client.controleur.Controleur;
import client.ihm.frame.affichage.MateZoneFrame;
import client.ihm.frame.connexion.ConnexionFrame;

/*-------------------------------*/
/* Classe IhmGui                 */
/*-------------------------------*/
/**
 * Classe IhmGui - Gère le lacnement et fermeture des difféentes frames.
 * Fait le lien entre Controleur -> ihm affin d'alléger le controleur.
 */
public class IhmGui 
{
	/*--------------------------*/
	/* Attributs                */
	/*--------------------------*/
	private Controleur controleur;

	private ConnexionFrame connexionFrame;
	private MateZoneFrame mateZoneFrame;

	/*--------------------------*/
	/* Constructeur             */
	/*--------------------------*/
	public IhmGui(Controleur controleur) 
	{
		this.controleur    = controleur;
		this.connexionFrame = null;
	}

	public void lancerConnexionFrame()
	{
		this.connexionFrame = new ConnexionFrame(this.controleur);
		this.connexionFrame.setVisible(true);
	}

	public void lancerMateZoneFrame(String pseudo) 
	{
		this.mateZoneFrame = new MateZoneFrame(this.controleur);
		this.mateZoneFrame.setVisible(true);
		this.connexionFrame.dispose();
	}

	/*--------------------------*/
	/* Affichage                */
	/*--------------------------*/
	public void afficherErreur(String message) 
	{
		JOptionPane.showMessageDialog(this.connexionFrame, message, "Erreur", JOptionPane.ERROR_MESSAGE);
	}

	public void afficherImg(byte[] bytes)
	{
		try 
		{
			if (bytes == null) 
			{
				this.afficherErreur("Image introuvable ou lecture impossible.");
				return;
			}
			
			// convert byte[] back to a BufferedImage
			InputStream is     = new ByteArrayInputStream(bytes);
			BufferedImage newBi = ImageIO.read(is);

			if (newBi == null) 
			{
				// ImageIO.read returns null when the input is not a known image format
				this.afficherErreur("Format d'image non reconnu ou données corrompues.");
				return;
			}

			// Save the image to disk
			java.io.File imageDir = new java.io.File("./image/");
			if (!imageDir.exists()) { imageDir.mkdirs(); } //create folder
			java.io.File outputFile = new java.io.File("./image/image_" + System.currentTimeMillis() + ".png");
			ImageIO.write(newBi, "png", outputFile);

		} catch (Exception e) { e.printStackTrace(); }
	}
}
