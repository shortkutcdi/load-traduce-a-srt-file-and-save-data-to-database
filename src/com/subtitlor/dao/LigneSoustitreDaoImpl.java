package com.subtitlor.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.subtitlor.beans.LigneSoustitre;

public class LigneSoustitreDaoImpl implements LigneSoustitreDao {
	private DaoFactory daoFactory;

	LigneSoustitreDaoImpl(DaoFactory daoFactory) {
		this.daoFactory = daoFactory;
	}

	public boolean chercher(LigneSoustitre ligneSoustitre) throws DaoException {
		Connection connexion = null;
		PreparedStatement statement = null;
		ResultSet resultat = null;
		boolean searchOk = false;
		try {
			connexion = daoFactory.getConnection();
			String sql = "SELECT numero, position_temps, texte FROM lignes_soustitres  WHERE numero=?  LIMIT 1;";
			statement = connexion.prepareStatement(sql);
			statement.setInt(1, ligneSoustitre.getNumeroLigne());
			resultat = statement.executeQuery();

			// compter le nombre de résultats
			int count = 0;
			resultat.last(); // se positionner sur le dernier resultset
			count = resultat.getRow(); // compter le nombre de lignes
			resultat.beforeFirst(); // ne pas oublier de se position avant le premier resultset pour les requêtes
									// suivantes
			if (count > 0) {
				searchOk = true;
			} else {
				searchOk = false;
			}
			connexion.commit();
		} catch (SQLException e) {
			if (connexion != null) {
				try {
					connexion.rollback();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			}
			throw new DaoException("Impossible de communiquer avec la base de données");
		} finally {
			try {
				if (connexion != null) {
					connexion.close();
				}
			} catch (SQLException e) {
				throw new DaoException("Impossible de communiquer avec la base de données");
			}
		}
		return searchOk;
	}


	public void ajouter(LigneSoustitre ligneSoustitre) throws DaoException {
		Connection connexion = null;
		PreparedStatement preparedStatement = null;

		try {
			connexion = daoFactory.getConnection();

			// on recherche si l'objet ligneSoustitre existe
			// si oui on fait un update sinon une insertion en BDD
			if (chercher(ligneSoustitre)) {
				String sqlUpdate = "UPDATE lignes_soustitres "
						+ "SET position_temps=?, texte=?, texte_original=?, nbr_lignes=? WHERE numero=? LIMIT 1;";
				preparedStatement = connexion.prepareStatement(sqlUpdate);
				preparedStatement.setString(1, ligneSoustitre.getPositionTemps());
				preparedStatement.setString(2, ligneSoustitre.getTexte());
				preparedStatement.setString(3, ligneSoustitre.getTexteOriginal());
				preparedStatement.setInt(4, ligneSoustitre.getNombreDeLignes());
				preparedStatement.setInt(5, ligneSoustitre.getNumeroLigne());
			} else {
				String sqlInsert = "INSERT INTO lignes_soustitres(numero, position_temps, texte, texte_original, nbr_lignes)"
						+ " VALUES(?, ?, ?, ?, ?);";
				preparedStatement = connexion.prepareStatement(sqlInsert);
				preparedStatement.setInt(1, ligneSoustitre.getNumeroLigne());
				preparedStatement.setString(2, ligneSoustitre.getPositionTemps());
				preparedStatement.setString(3, ligneSoustitre.getTexte());
				preparedStatement.setString(4, ligneSoustitre.getTexteOriginal());
				preparedStatement.setInt(5, ligneSoustitre.getNombreDeLignes());
			}

			preparedStatement.executeUpdate();
			// ici on enregistre nos transactions en bdd qu'à la fin de toutes nos
			// modifications
			connexion.commit();
		} catch (SQLException e) {
			if (connexion != null) {
				try {
					connexion.rollback();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			}
			throw new DaoException("Impossible de communiquer avec la base de données ");
		} finally {
			try {
				if (connexion != null) {
					connexion.close();
					//System.out.println("Fermeture connexion");
				}
			} catch (SQLException e) {
				throw new DaoException("Impossible de communiquer avec la base de données");
			}
		}
	}

	public List<LigneSoustitre> lister() throws DaoException {
		List<LigneSoustitre> lignesSoustitre = new ArrayList<LigneSoustitre>();
		Connection connexion = null;
		Statement statement = null;
		ResultSet resultat = null;

		try {
			connexion = daoFactory.getConnection();
			statement = connexion.createStatement();
			String sql = "SELECT numero, position_temps, texte, texte_original, nbr_lignes FROM lignes_soustitres ORDER BY numero;";
			resultat = statement.executeQuery(sql);

			while (resultat.next()) {
				int numero = resultat.getInt("numero");
				String positionTemps = resultat.getString("position_temps");
				String texte = resultat.getString("texte");
				String texteOriginal = resultat.getString("texte_original");
				int nombreDeLignes = resultat.getInt("nbr_lignes");

				LigneSoustitre ligneSoustitre = new LigneSoustitre();
				ligneSoustitre.setNumeroLigne(numero);
				ligneSoustitre.setPositionTemps(positionTemps);
				ligneSoustitre.setTexte(texte);
				ligneSoustitre.setTexteOriginal(texteOriginal);
				ligneSoustitre.setNombreDeLignes(nombreDeLignes);

				lignesSoustitre.add(ligneSoustitre);
			}
			connexion.commit();
		} catch (SQLException e) {
			if (connexion != null) {
				try {
					connexion.rollback();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			}
			throw new DaoException("Impossible de communiquer avec la base de données "
					+ "; causes possibles : \nidentifiants incorrects ou requête erronée ");
		} finally {
			try {
				if (connexion != null) {
					connexion.close();
				}
			} catch (SQLException e) {
				throw new DaoException("Impossible de communiquer avec la base de données");
			}
		}
		return lignesSoustitre;
	}

	@Override
	public void supprimer() throws DaoException {
		Connection connexion = null;
		PreparedStatement preparedStatement = null;

		try {
			connexion = daoFactory.getConnection();
			Statement statement = connexion.createStatement();
			int resultat = statement.executeUpdate("DELETE FROM oc_javaee.lignes_soustitres where id >0;");

			connexion.commit();
		} catch (SQLException e) {
			if (connexion != null) {
				try {
					connexion.rollback();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			}
			throw new DaoException("Impossible de communiquer avec la base de données ");
		} finally {
			try {
				if (connexion != null) {
					connexion.close();
				}
			} catch (SQLException e) {
				throw new DaoException("Impossible de communiquer avec la base de données");
			}
		}

	}

}