'use strict';

App.controller('LoginController',['$scope','$window','LoginService',function($scope,$window,LoginService){

 
     var url='http://localhost:8080/PathFinder';
   
     window.onload=function(){
    	 self.build();
     }
     
     $scope.submit=function(){
    	
    	 self.eval();
     }
     
     self.eval=function(){
    	
    	 var r=$scope.rule;
    	
    	 LoginService.eval(r)
    	 .then(
  			   function(data){
  				$scope.result=data.response;
  				 
  			   },
  	              function(errResponse){
  	               console.error('Error while creating User.');

                }	);
     },
     
     
     
   self.build=function(){
	   LoginService.build()
	   .then(
			   function(data){
				   $scope.message="Path Finder Service Instantiated" +
				"Test interattivo per il PFS memorizzato nella tabella PFS.polynomials"+
				 "Valutazione -> : 'eval:<requestorId>,<ownerId>,<depth>'"+
				"Inserimento -> : 'ins:<userId>@[<contactId>#]'"+
				 	
				 "PFS#";

			   },
	              function(errResponse){
	               console.error('Error while creating User.');

              }	);
   }
}]);