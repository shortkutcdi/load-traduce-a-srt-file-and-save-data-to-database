package com.subtitlor.beans;

public class LigneSoustitre {

	private int numeroLigne;
	private String positionTemps;
	private String texte;
	private String texteOriginal;
	private int nombreDeLignes;
	private String[] lignesDeTexte;
	private String[] lignesDeTexteOriginal;

	public String getTexteOriginal() {
		return texteOriginal;
	}

	public void setTexteOriginal(String texteOriginal) {
		this.texteOriginal = texteOriginal;
	}

	public String[] getLignesDeTexteOriginal() {
		lignesDeTexteOriginal = this.texteOriginal.split("//");
		return lignesDeTexteOriginal;
	}

	public void setLignesDeTexteOriginal(String[] lignesDeTexteOriginal) {
		this.lignesDeTexteOriginal = lignesDeTexteOriginal;
	}

	public int getNombreDeLignes() {
		return nombreDeLignes;
	}

	public void setNombreDeLignes(int nombreDeLignes) {
		this.nombreDeLignes = nombreDeLignes;
	}

	public void setLignesDeTexte(String[] lignesDeTexte) {
		this.lignesDeTexte = lignesDeTexte;
	}

	public int getNumeroLigne() {
		return numeroLigne;
	}

	public void setNumeroLigne(int numeroLigne) {
		this.numeroLigne = numeroLigne;
	}

	public String getPositionTemps() {
		return positionTemps;
	}

	public void setPositionTemps(String positionTemps) {
		this.positionTemps = positionTemps;
	}

	public String getTexte() {
		return texte;
	}

	public void setTexte(String texte) {
		this.texte = texte;
	}

	public String[] getLignesDeTexte() {
		lignesDeTexte = this.texte.split("//");
		return lignesDeTexte;
	}

	@Override
	public String toString() {
		return "LigneSoustitre [numeroLigne=" + numeroLigne + ", positionTemps=" + positionTemps + ", nombreDeLignes="
				+ nombreDeLignes + ", texte=" + texte + ", texte original=" + texteOriginal + "]";
	}

}
