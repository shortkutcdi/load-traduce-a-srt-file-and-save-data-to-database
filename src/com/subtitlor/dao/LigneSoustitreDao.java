package com.subtitlor.dao;

import java.util.List;

import com.subtitlor.beans.LigneSoustitre;

public interface LigneSoustitreDao {
	
	/**
	 * vérifier s'il existe un sous-titre - test sur 'numero' qui est unique en bdd
	 * @param ligneSoustitre
	 * @return 
	 * @throws DaoException
	 */
	boolean chercher(LigneSoustitre ligneSoustitre) throws DaoException;
	
	/**
	 * ajouter un enregistrement en base de données
	 * @param ligneSoustitre objet représentant une ligne de sous-titre traduite
	 * @throws DaoException
	 */
	void ajouter(LigneSoustitre ligneSoustitre) throws DaoException;
	
	/**
	 * Lister tous les enregistrement en base de données
	 * @return
	 * @throws DaoException
	 */
	List<LigneSoustitre> lister() throws DaoException;
	
	/**
	 * Supprimer tous les enregistrements en base de données
	 * @throws DaoException
	 */
	void supprimer() throws DaoException;
}
