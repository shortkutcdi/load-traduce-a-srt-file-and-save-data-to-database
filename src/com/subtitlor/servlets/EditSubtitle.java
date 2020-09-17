package com.subtitlor.servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.subtitlor.beans.LigneSoustitre;
import com.subtitlor.dao.DaoException;
import com.subtitlor.dao.DaoFactory;
import com.subtitlor.dao.LigneSoustitreDao;
import com.subtitlor.utilities.SubtitlesHandler;
import com.subtitlor.utilities.SubtitlesHandlerException;

@WebServlet("/EditSubtitle")
public class EditSubtitle extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private static final String FILE_NAME = "/WEB-INF/fichier-soustitres.srt";
	private static final String FILE_TRAD = "traduction.srt";
	private static final String ERREUR = "erreur";
	private static final String VUE = "/WEB-INF/edit_subtitle.jsp";

	private LigneSoustitreDao ligneSoustitreDao;

	@Override
	public void init() throws ServletException {
		DaoFactory daoFactory = DaoFactory.getInstance();
		this.ligneSoustitreDao = daoFactory.getLigneSoustitreDao();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException{
		
		ServletContext context = getServletContext();
		System.out.println(context.getRealPath(FILE_NAME));
		
		String file = FILE_NAME;
		
		// récupérer objet de gestion des sous-titres
		// à l'instantiation il va lire un fichier srt et récuprer les données 
		// ou le créer s'il n'existe pas
		SubtitlesHandler subtitles = null;
		String erreur = "";
		try {
			subtitles = new SubtitlesHandler(context.getRealPath(file));
			System.out.println("sous titres ok");
		} catch (SubtitlesHandlerException e1) {
			System.out.println("sous titres erreur" + e1.getMessage());
			erreur = e1.getMessage();
			request.setAttribute(ERREUR, erreur);
			this.getServletContext().getRequestDispatcher(VUE).forward(request, response);
		}
		
		// interroger la base de données pour récupérer les traductions
		List<LigneSoustitre> traductions = null;
		try {
			traductions = ligneSoustitreDao.lister();
			if (traductions != null) {
				// envoyer les traductions à la vue
				request.setAttribute("traductions", traductions);
				System.out.println("ok traductions");
			} else {
				System.out.println("erreur traductions");
				throw new SubtitlesHandlerException("pas de données à afficher, veuillez charger "
													+ "un fichier srt avec des données");
			}
		} catch (DaoException e) {
			e.printStackTrace();
			request.setAttribute(ERREUR, e.getMessage());
		} catch (SubtitlesHandlerException e1) {
			request.setAttribute(ERREUR, e1.getMessage());
		}

		try {
			// envoyer les sous-titres non traduits à la vue
			request.setAttribute("subtitles", subtitles.getOriginalLineSubtitle());
		} catch (NullPointerException | SubtitlesHandlerException e ) {
			request.setAttribute(ERREUR, e.getMessage());
		} 
		
		if (erreur.isEmpty()) {
			 this.getServletContext().getRequestDispatcher(VUE).forward(request, response);
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		ServletContext context = getServletContext();
		SubtitlesHandler subtitles = null;
		ArrayList<LigneSoustitre> translatedSubtileList = null;
		try {
			subtitles = new SubtitlesHandler(context.getRealPath(FILE_NAME));
			subtitles.enregistrerFichierTraduction(FILE_TRAD, request);

			translatedSubtileList = subtitles.getTranslatedSubtitles(request);
		} catch (SubtitlesHandlerException e1) {
			request.setAttribute(ERREUR, e1.getMessage());
		}

		for (LigneSoustitre ligneTraduction : translatedSubtileList) {
			try {
				ligneSoustitreDao.ajouter(ligneTraduction);
				// confirmation d'enregisrement en base de données
				request.setAttribute("validation", "ok");
			} catch (DaoException e) {
				e.printStackTrace();
				request.setAttribute(ERREUR, e.getMessage());
			}
		}

		try {
			// envoyer les sous-titres non traduits à la vue
			request.setAttribute("subtitles", subtitles.getOriginalLineSubtitle());
		} catch (SubtitlesHandlerException e) {
			request.setAttribute(ERREUR, e.getMessage());
		}
		// envoi du tableau des traductions à la vue
		request.setAttribute("traductions", translatedSubtileList);

		this.getServletContext().getRequestDispatcher(VUE).forward(request, response);
	}

}
