<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Page d'accueil</title>
</head>
<body>
	<h2>Page d'accueil</h2>
	<hr>
	<p>
	Bienvenue sur notre page d'accueil !
	</p>
	<hr>
	<!-- afficher nom du'utilisateur et rôle -->
	<p>
	Utilisateur : <security:authentication property="principal.username" />
	</p>
	<p>
	Roles(s) : <security:authentication property="principal.authorities"/>
	<!-- authorities est équivalent au rôle -->
	</p>
	<hr>
	<!-- page "/managers" réservée aux managers -->
	<security:authorize access="hasRole('MANAGER')">
		<p>
			<a href="${pageContext.request.contextPath}/managers">Réunion managers</a>
			(Réservé aux managers)
		</p>
	</security:authorize>
	<!-- page "/admins" réservée aux admins -->
	<security:authorize access="hasRole('ADMIN')">
		<p>
			<a href="${pageContext.request.contextPath}/admins">Réunion admins</a>
			(Réservé aux admins)
		</p>
	</security:authorize>
	<!-- Ajout du bouton logout -->
	<form:form action="${pageContext.request.contextPath}/logout" method="POST">
		<input type="submit" value="Se déconnecter" />
	</form:form>
</body>
</html>