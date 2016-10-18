'use strict';

App.controller('LoginController',['$scope','$window','LoginService',function($scope,$window,LoginService){
	var self=this;
        self.user={id:null,birth_day:'',city:'',email:'',firstname:'',lastname:'',photo:'',pw:''};
     var message;
   
     // var url='http://193.206.170.147/PathFinder';
     //var path='http://localhost:8080/PathFinder';

   
    $window.onload=function(){
	
   /*	LoginService.prova()
    	.then(
    			function(data){
    				
    			},
    			function(errResponse){
		               console.error('Error while creating User.');

	              }	
		);*/
		

    }
	
        
}]);