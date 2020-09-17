package com.subtitlor.utilities;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.net.HttpRetryException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import com.subtitlor.beans.LigneSoustitre;

public class SubtitlesHandler {
	private ArrayList<LigneSoustitre> originalSubtitles = null;
	private ArrayList<LigneSoustitre> translatedSubtitles = null;

	/**
	 * gestion des sous-titres à partir du fichier 'filename'
	 * si fichier existant avec données alimentation du tableau originalSubtitles
	 * si fichier absent - création d'un nouveau fichier srt
	 * @param fileName
	 * @throws SubtitlesHandlerException
	 */
	public SubtitlesHandler(String fileName) throws SubtitlesHandlerException {
		originalSubtitles = new ArrayList<>();
		BufferedReader br;
		try {
			File file = new File(fileName);
			if (!file.exists()) {
				file.createNewFile();
				throw new SubtitlesHandlerException(
						"Le fichier de traduction est vide (ou absent), veuillez charger un fichier srt avec des données");
			}if (file.length() == 0) {
				throw new SubtitlesHandlerException(
						"Le fichier de traduction est vide (ou absent), veuillez charger un fichier srt avec des données");
			} else {
				{
					br = new BufferedReader(new FileReader(fileName));
					// initialisation des variables
					String line;
					int count = 0;
					boolean isText = false;
					LigneSoustitre lineSubtitle = null;

					String texte = "";
					int nbreLignes = 0;
					while ((line = br.readLine()) != null) {
						// regex pour détecter le format de la ligne positionTemps
						// format de la forme : 00:01:48,943 --> 00:01:51,191
						Pattern posTemps = Pattern
								.compile("^([0-9]{2}:){2}[0-9]{2},[0-9]{3} --> ([0-9]{2}:){2}[0-9]{2},[0-9]{3}$");
						// regex pour détecter le numéro de ligne						
						Pattern numLigne = Pattern.compile("^[0-9]+$");
						
						// 1- première ligne d'un sous-titre - numéro
						if (count == 0) {
							if (numLigne.matcher(line).find() && !isText) {
								lineSubtitle = new LigneSoustitre();
								lineSubtitle.setNumeroLigne(Integer.parseInt(line));
							}
						// 2- position temps							
						} else if (count == 1 && posTemps.matcher(line).find() && !isText) {
							isText = true;
							lineSubtitle.setPositionTemps(line);
						// 3- lignes de texte et ligne vide (termine le sous-titre)							
						} else if (count == 2 && isText && (line != null || line.isEmpty())) {
							nbreLignes = 1;
							texte = line;

						} else if (count > 2 && isText) {
							if (!line.isEmpty()) {
								nbreLignes++;
								texte += "//" + line;
							} else {
								// 4- si ligne vide (fin de sous-titre)
								// récupérations données pour lineSubtitle 
								// puis ajout lineSubtitle à l'array originalSubtitles
								lineSubtitle.setNombreDeLignes(nbreLignes);
								lineSubtitle.setTexte(texte);
								lineSubtitle.setTexteOriginal(texte);
								String[] lignesDeTexte = texte.split("//");
								lineSubtitle.setLignesDeTexteOriginal(lignesDeTexte);
								isText = false;
								count = -1;

								originalSubtitles.add(lineSubtitle);
							}
						}
						count++;
					}
					br.close();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
			throw new SubtitlesHandlerException(
					"Fichier de sous-titre absent, aller à l'accueil et charger un fichier de sous-titres");
		}
	}

	/**
	 * récupère les sous-titres à traduire dans un ArrayList
	 * @return
	 * @throws SubtitlesHandlerException
	 */
	public ArrayList<LigneSoustitre> getOriginalLineSubtitle() throws SubtitlesHandlerException {
		return originalSubtitles;
	}

	/**
	 * récupère les sous-titres traduis dans un ArrayList
	 * @param request
	 * @return
	 * @throws SubtitlesHandlerException
	 */
	public ArrayList<LigneSoustitre> getTranslatedSubtitles(HttpServletRequest request)
			throws SubtitlesHandlerException {
		translatedSubtitles = new ArrayList<>();

		if (originalSubtitles != null) {

			for (LigneSoustitre ligneSoustitre : originalSubtitles) {

				LigneSoustitre ligneTraduction = new LigneSoustitre();
				ligneTraduction.setNumeroLigne(ligneSoustitre.getNumeroLigne());
				ligneTraduction.setPositionTemps(ligneSoustitre.getPositionTemps());
				ligneTraduction.setNombreDeLignes(ligneSoustitre.getNombreDeLignes());
				ligneTraduction.setTexteOriginal(ligneSoustitre.getTexteOriginal());
				ligneTraduction.setLignesDeTexteOriginal(ligneSoustitre.getLignesDeTexteOriginal());

				// gestion de la propriété texte (fonction du nombre de lignes)
				// ajout de "//" pour détecter les lignes de texte
				gestionLigneTexte(request, ligneSoustitre, ligneTraduction);

				translatedSubtitles.add(ligneTraduction);
			}
		}
		return translatedSubtitles;
	}

	/**
	 * création d'un fichier srt de traductions
	 * @param fileName
	 * @param request
	 * @throws IOException
	 * @throws SubtitlesHandlerException
	 */
	public void enregistrerFichierTraduction(String fileName, HttpServletRequest request)
			throws IOException, SubtitlesHandlerException {
		List<LigneSoustitre> translatedLineSubtitles2 = this.getTranslatedSubtitles(request);

		if (translatedLineSubtitles2 != null) {

			String fileSeparator = System.getProperty("file.separator");
			
			// création du répertoire c:/temp s'il n'existe pas
			if (!new File("c:" + fileSeparator + "temp").exists()) {
				// créer le dossier avec tous ses parents
				new File("c:" + fileSeparator + "temp").mkdirs();
			}
			
			String absoluteFilePath = "c:" + fileSeparator + "temp" + fileSeparator + fileName;
			
			// création du fichier s'il n'existe pas
			File file = new File(absoluteFilePath);
			if (file.createNewFile()) {
				System.out.println("File created");
			} 
//			else {
//				System.out.println("File already exists");
//			}
			// définition d'un fichier
			FileWriter fw = null;
			// la définition du writer doit se faire ici
			// pour des raisons de visibilité
			BufferedWriter buffwriter = null;

			try {

				fw = new FileWriter(file);
				buffwriter = new BufferedWriter(fw);

				for (LigneSoustitre ligneSoustitre : translatedLineSubtitles2) {

					buffwriter.write(String.valueOf(ligneSoustitre.getNumeroLigne()));
					buffwriter.newLine();
					buffwriter.write(ligneSoustitre.getPositionTemps());
					buffwriter.newLine();
					// enregistrer les lignes
					String[] lignesDeTexte = ligneSoustitre.getTexte().split("//");
					for (String lignedetexte : lignesDeTexte) {
						buffwriter.write(lignedetexte);
						buffwriter.newLine();
					}
					buffwriter.newLine();
				}
				buffwriter.close();
			} catch (IOException e) {
				throw new SubtitlesHandlerException("Problème de fichier");
			}
		}
	}

	/**
	 * récupérer les informations de texte à traduire et concaténer si plusieurs
	 * lignes avec "//"
	 * 
	 * @param request
	 * @param ligneSoustitre
	 * @param ligneTraduction
	 */
	private void gestionLigneTexte(HttpServletRequest request, LigneSoustitre ligneSoustitre,
			LigneSoustitre ligneTraduction) {

		String[] lignesDeTexte = ligneSoustitre.getTexte().split("//");
		String[] ligneDeTrad = new String[lignesDeTexte.length];
		String texte = "";
		
		for (int i = 1; i <= lignesDeTexte.length; i++) {
			String ligneSoustitree = (String) request
					.getParameter(Integer.toString(ligneSoustitre.getNumeroLigne()) + "-" + i + "-texte");
			if (i == 1) {
				texte += ligneSoustitree.isEmpty() ? "" : ligneSoustitree;
			} else {
				texte += ligneSoustitree.isEmpty() ? "" : "//" + ligneSoustitree;
				request.setAttribute(ligneSoustitre.getNumeroLigne() + "-" + i + "-texte", texte);
			}
			ligneDeTrad[i - 1] = texte;
		}
		ligneTraduction.setLignesDeTexte(ligneDeTrad);
		if (texte.equals("")) {
			// ajout \\ pour une ligne vide non traduite
			ligneTraduction.setTexte("//");
		} else {
			ligneTraduction.setTexte(texte);
		}
	}

}
