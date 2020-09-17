package com.subtitlor.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DaoFactory {
	private static final String HOST_DB_NAME = "jdbc:mysql://localhost:3306/oc_javaee?serverTimezone=UTC";
	private static final String USER_LOGIN = "root";
	private static final String PASSWORD = "root";

	private String url;
	private String username;
	private String password;

	private DaoFactory(String url, String username, String password) {
		this.url = url;
		this.username = username;
		this.password = password;
	}

	public static DaoFactory getInstance() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		DaoFactory instance = new DaoFactory(HOST_DB_NAME, USER_LOGIN, PASSWORD);

		return instance;
	}

	public Connection getConnection() throws SQLException {
		Connection connexion = DriverManager.getConnection(url, username, password);
		// gérer les transaction à la main
		connexion.setAutoCommit(false);
		return connexion;
	}

	// récupération du DAO
	public LigneSoustitreDao getLigneSoustitreDao() {
		return new LigneSoustitreDaoImpl(this);
	}
}
