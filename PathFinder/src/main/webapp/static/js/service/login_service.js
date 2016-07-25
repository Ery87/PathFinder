'use strict';

App.factory('LoginService',['$http','$q',function($http,$q){
	var path='http://localhost:8080/PathFinder';
	
	return{
		eval:function(r){
			return $http.post(path+'/eval/',r)
			.then(
					function(response){
						return response.data;
					},
					function(errResponse){
						console.error('Error while creating user');
						return $q.reject(errResponse);
					});
	    },


		
			
		    build:function(){
		    	var id=12;
				return $http.post(path+'/profile/',id)
				
				.then(
						function(response){
							return response.data;
						},
						function(errResponse){
							console.error('Error while creating user');
							return $q.reject(errResponse);
						});
		    }
	
};
	}



]);


