<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
  <head>  
    <script src="https://ajax.googleapis.com/ajax/libs/angularjs/1.4.4/angular.js"></script>
      <script src="https://ajax.googleapis.com/ajax/libs/angularjs/1.4.4/angular-route.js"></script>
      <script src="<c:url value='/static/js/libraries/rollups/aes.js' />"></script>
  <script src="<c:url value='/static/js/libraries/components/enc-base64-min.js' />"></script>
      
    <title>Welcome to DSNProject</title>  
  
     <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap.min.css">
     <link href="<c:url value='/static/css/app.css' />" rel="stylesheet"></link>
     
  </head>
  <body ng-app="myApp" class="ng-cloak">
 
      <div class="generic-container" ng-controller="LoginController as ctrl">
      <div>Path Finder Service</div>
      <script src="<c:url value='/static/js/app.js' />"></script>
      <script src="<c:url value='/static/js/service/login_service.js' />"></script>
      <script src="<c:url value='/static/js/controller/login_controller.js' />"></script>
  </body>
</html>