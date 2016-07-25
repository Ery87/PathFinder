'use strict';
	


App.service('EvalService', ['$http','$q',function ($http,$q) {
	var path='http://localhost:8080/PathFinder';

	return{
		
		
		 eval:function(utente){
		     
		       return  $http.post(path+'/profile/',utente).then(
						function(response){
							return response.data;
						}, 
						function(errResponse){
							console.error($q.reject(errResponse));
							return $q.reject(errResponse);
						}
				);
		        
		        
		    },
		   
		
		
	};
}
   
]);
