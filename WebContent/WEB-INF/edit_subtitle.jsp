<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8" />
<title>Editer les sous-titres</title>
</head>
<body style="padding-top: 40px;">

	<c:if test="${ !empty erreur }">
		<p style="color: red;"><c:out value="${ erreur }" /></p>
		
		<p>
			<a href="upload">Charger un fichier .srt de sous-titres à traduire</a>
		</p>
		<p  style="position: fixed; top: 10px; left: 10px; background-color: white; width: 120px;" >Aller à l'<a href="index.jsp">accueil</a></p>
	</c:if>
	<c:if test="${ empty erreur }">
		<p  style="position: fixed; top: 10px; left: 10px; background-color: white;  width: 120px;" >Aller à l'<a href="index.jsp">accueil</a></p>
	</c:if>

	<c:if test="${ validation == 'ok' }">
		<h4 style="position: fixed; top: 30px; right: 10px;">Traduction	enregistrée en base de données</h4>
		<h4 style="position: fixed; top: 70px; right: 10px;">
			Fichier <em>traduction.srt</em> généré ou mis à jour <br />à l'emplacement <em>c:/temp</em>
		</h4>
	</c:if>
	
	<c:if test="${ empty erreur }">
		<form method="post"  style="margin-top: 60px; padding-left: 60px;" >
	
				<input type="submit" style="position: fixed; top: 10px; right: 10px;" value="Soumettre la requête"/>
			<table>
				<c:choose>
					<c:when test="${ empty traductions }">
						<c:forEach items="${ subtitles }" var="line" varStatus="status11">
							<tr>
								<td style="text-align: right;">
									<c:out	value="${ line.numeroLigne }" />
								</td>
								<td>
									<input type="hidden" name="${ status11.count }-numLigne"
											value="${ line.numeroLigne }" />
								</td>
							<tr>
								<td style="text-align: right;">
									<c:out value="${ line.positionTemps }" />
								</td>
								<td>
									<input type="hidden" name="${ status11.count }-postTemps"
									value="${ line.positionTemps }" />
								</td>
							</tr>
							<c:forEach items="${ line.lignesDeTexte }" var="ligne"
								varStatus="status12">
								<tr>
									<td style="text-align: right;"><c:out value="${ ligne }" /></td>
									<td>
										<input type="text" name="${ status11.count }-${ status12.count }-texte"
										id="${ status11.count }-${ status12.count }-texte" size="35" />
									</td>
								</tr>
							</c:forEach>
							<tr>
								<td>&nbsp;</td>
								<td>&nbsp;</td>
							</tr>
						</c:forEach>
					</c:when>
					<c:otherwise>
						<c:forEach items="${ traductions }" var="lignetrad" varStatus="status">
							<tr>
								<td style="text-align: right;">
									<c:out value="${ lignetrad.numeroLigne }" /></td>
								<td>
									<input type="hidden" name="${ status.count }-numLigne"
										value="<c:out value="${ lignetrad.numeroLigne }" />" />
								</td>
							<tr>
								<td style="text-align: right;"><c:out
										value="${ lignetrad.positionTemps }" /></td>
								<td><input type="hidden" name="${ status.count }-postTemps"
									value="<c:out value="${ lignetrad.positionTemps }" />" /></td>
							</tr>
							<c:forEach items="${ lignetrad.lignesDeTexteOriginal }"
								var="ligneoriginal" varStatus="statuslignorigin">
								<tr>
									<td style="text-align: right;"><c:out
											value="${ ligneoriginal }" /></td>
	
									<c:set var="lignesTraduite" value="${ lignetrad.lignesDeTexte }" />
									<c:set var="ligneTraduite"
										value="${ lignesTraduite[statuslignorigin.index] }" />
	
									<td>
										<input type="text" name="${ status.count }-${ statuslignorigin.count }-texte"
											size="35" value="${ ! empty ligneTraduite ?  ligneTraduite : " " }" />
									</td>
								</tr>
							</c:forEach>
	
							<tr>
								<td>&nbsp;</td>
								<td>&nbsp;</td>
							</tr>
						</c:forEach>
	
					</c:otherwise>
				</c:choose>
				<tr>
					<td>&nbsp;</td>
					<td>&nbsp;</td>
				</tr>
	
			</table>
		</form>
	</c:if>
</body>
</html>