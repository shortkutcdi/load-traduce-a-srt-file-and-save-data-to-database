package com.subtitlor.servlets;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

import com.subtitlor.dao.DaoException;
import com.subtitlor.dao.DaoFactory;
import com.subtitlor.dao.LigneSoustitreDao;

/**
 * Servlet implementation class UploadFichier
 */
@WebServlet("/UploadFichier")
public class UploadFichier extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public static final int TAILLE_TAMPON = 10240;
	// SEPARATOR : permet d'afficher le séparateur "/" ou "\" 
	// et ne pas dépendre ainsi de l'OS
	private static final String  SEPARATOR = System.getProperty("file.separator");	
	private static final String CHEMIN_FICHIERS = "/WEB-INF/";
	private static final String FILE_NAME = "fichier-soustitres.srt";
	private static final String VUE = "/WEB-INF/uploadfichier_a_traduire_srt.jsp";	
	
	private LigneSoustitreDao ligneSoustitreDao;

	@Override
	public void init() throws ServletException {
		DaoFactory daoFactory = DaoFactory.getInstance();
		this.ligneSoustitreDao = daoFactory.getLigneSoustitreDao();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		this.getServletContext().getRequestDispatcher( VUE ).forward(request,	response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		String fileSeparator = System.getProperty("file.separator");
		
		// création du répertoire c:/temp s'il n'existe pas
		if (!new File("c:" + fileSeparator + "temp").exists()) {
			// créer le dossier avec tous ses parents
			new File("c:" + fileSeparator + "temp").mkdirs();
		}

		System.out.println("dans doPost  upload");
		// On récupère le champ description comme d'habitude
		String description = request.getParameter("description");
		request.setAttribute("description", description);

		// On récupère le champ du fichier
		Part part = request.getPart("fichier");

		// On vérifie qu'on a bien reçu un fichier
		String nomFichier = getNomFichier(part);
		

		// Si on a bien un fichier
		if (nomFichier != null && !nomFichier.isEmpty()) {
			String nomChamp = part.getName();
			// Corrige un bug du fonctionnement d'Internet Explorer
			nomFichier = nomFichier.substring(nomFichier.lastIndexOf('/') + 1)
					.substring(nomFichier.lastIndexOf('\\') + 1);

			// On écrit définitivement le fichier sur le disque
			ecrireFichier(part, FILE_NAME, CHEMIN_FICHIERS);

			HttpSession session = request.getSession();
			session.setAttribute("fichier", CHEMIN_FICHIERS + nomFichier);

			// effacer les données dans la base de données
			try {
				ligneSoustitreDao.supprimer();
			} catch (DaoException e) {
				e.printStackTrace();
			}
			request.setAttribute("post", "post");
			
			request.setAttribute(nomChamp, nomFichier);
		}

		this.getServletContext().getRequestDispatcher( VUE ).forward(request, response);
	}

	private void ecrireFichier(Part part, String nomFichier, String chemin) throws IOException {
		BufferedInputStream entree = null;
		BufferedOutputStream sortie = null;
		ServletContext context = getServletContext();
		System.out.println(context.getRealPath(chemin + nomFichier));

		try {
			ServletConfig scfg = getServletConfig();
			ServletContext scxt = scfg.getServletContext();
			String webInfPath = scxt.getRealPath("WEB-INF");

			File file = new File(webInfPath + SEPARATOR + nomFichier);
			
			if (file.createNewFile()) {
				System.out.println("File created");
			} else {
				if (file.delete()) {
					System.out.println(file.getName() + " est supprimé.");
				} else {
					System.out.println("Opération de suppression echouée");
				}
				file = new File(webInfPath + SEPARATOR + nomFichier);
				//System.out.println("File already exists");
			}
			entree = new BufferedInputStream(part.getInputStream(), TAILLE_TAMPON);
			// sortie = new BufferedOutputStream(new FileOutputStream(new
			// File(context.getRealPath(chemin + nomFichier))), TAILLE_TAMPON);
			sortie = new BufferedOutputStream(
								new FileOutputStream(
									new File(webInfPath + SEPARATOR + nomFichier)),
												TAILLE_TAMPON);

			byte[] tampon = new byte[TAILLE_TAMPON];
			int longueur;
			while ((longueur = entree.read(tampon)) > 0) {
				sortie.write(tampon, 0, longueur);
			}

		} finally {
			try {
				sortie.close();
			} catch (IOException ignore) {
			}
			try {
				entree.close();
			} catch (IOException ignore) {
			}
		}
	}

	private static String getNomFichier(Part part) {
		for (String contentDisposition : part.getHeader("content-disposition").split(";")) {
			if (contentDisposition.trim().startsWith("filename")) {
				return contentDisposition.substring(
						contentDisposition.indexOf('=') + 1).trim().replace("\"", "");
			}
		}
		return null;
	}
}