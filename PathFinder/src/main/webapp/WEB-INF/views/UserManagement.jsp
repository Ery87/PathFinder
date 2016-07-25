<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
  <head>  
    <title>Welcome to DSNProject</title>  
    <style>
      .username.ng-valid {
          background-color: lightgreen;
      }
      .username.ng-dirty.ng-invalid-required {
          background-color: red;
      }
      .username.ng-dirty.ng-invalid-minlength {
          background-color: yellow;
      }
      .email.ng-valid {
          background-color: lightgreen;
      }
      .email.ng-dirty.ng-invalid-required {
          background-color: red;
      }
      .email.ng-dirty.ng-invalid-email {
          background-color: yellow;
      }
    </style>
     <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap.min.css">
     <link href="<c:url value='/static/css/app.css' />" rel="stylesheet"></link>
     
  </head>
  <body ng-app="myApp" class="ng-cloak">
 
      <div class="generic-container" ng-controller="LoginController">
        <h1 style="text-align:center;">Welcome to DSNProject!</h1>
          <div class="panel panel-default">
             
            <p>{{message}}</p>
                  <form name="form"  class="form-horizontal" >
   <input type="text" ng-model="rule" name="rule" class="rule form-control input-sm" placeholder="Write your access rule" />  
       <input type="submit" ng-click="submit()" class="btn btn-primary btn-sm" ng-disabled="myForm.$invalid" value="invia"/>
   
         
               </form>
           {{result}}
          </div>
        </div>
        </div>
            <script src="<c:url value='/static/js/libraries/aes.js' />"></script>
             <script src="<c:url value='/static/js/libraries/enc-base64-min.js' />"></script>
      
      <script src="https://ajax.googleapis.com/ajax/libs/angularjs/1.4.4/angular.js"></script>
      <script src="https://ajax.googleapis.com/ajax/libs/angularjs/1.4.4/angular-route.js"></script>
      <script src="<c:url value='/static/js/app.js' />"></script>
      <script src="<c:url value='/static/js/service/login_service.js' />"></script>
      <script src="<c:url value='/static/js/controller/login_controller.js' />"></script>
  </body>
</html>