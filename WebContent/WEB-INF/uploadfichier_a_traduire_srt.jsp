<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Charger les sous-titres</title>
</head>
<body>

	<h1>Charger un nouveau fichier de sous-titres srt pour le traduire</h1>
	<p style="color: red;">Attention les données en base de données et le fichier de traduction vont être supprimé</p>
	<p style="color: red;">Pensez à exporter la base de donnée et enregistrer votre ancien fichier de traduction 
		<br />avant de charger le nouveau fichier de sous-titres</p>
		
	<p>Aller à l'<a href="index.jsp">accueil</a></p>
	
	<form  method="post"  enctype="multipart/form-data">
		<input type="hidden" name="description" id="description" value=""/>		
		<p>
			<label for="fichier">Fichier srt pour traduction :</label>
			<input type="file" name="fichier" id="fichier" />		
		</p>
		<input type="submit" value="Charger les sous-titres" />
	</form>
	
	<c:if test="${! empty post }">
		<p style="color: #2d6ace;">Le fichier de sous-titre est chargé</p>
		<h2><a href="edit">Faire la traduction du fichier de sous-titres</a></h2>
	</c:if>
</body>
</html>