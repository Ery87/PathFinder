'use strict';

App.factory('LoginService',['$http','$q',function($http,$q){
	 //var path='http://193.206.170.147/PathFinder';
	  // var path='http://localhost:8080/PathFinder';
	return{
		/* prova:function(){
		     var id="9eebe1874ef8c4db459a9306250ae659071b21749b35311674e530b1eabfaf5a934f99bf5570320e1f0e33156014620c3d433f36053a92b8de7184b7c7c8df43c60a7d9ae90f4c3d1f0393534af067497b63b2fc0a8da8e5ec3c7de26b42e8276b0453624951eda12d37f98c949b3964bc3ab4b220ba1e68687926364911b0a025c5687727e5acbda077fbfaae93fbdfa26614506a7104aafa4c2175f447a9cdb79e20940f855a9f4acd60ab31cf34432d49c554655c2b9ef003bc5b6d44043a5c75c0609988ff98275b0c2ac575222ebb41fbd41844bcd73266e362ae4a36cf4b5a582e8d0dfd385e49a61bf22c47f2329bc7cc644792b78aa84479a52fb191";
		       return  $http.get(path+'/friendshipCreation/'+id).then(
						function(response){
							console.log(response.data);
							return response.data
						},
					    			
						function(errResponse){
							console.error('Error while creating user');
							return $q.reject(errResponse);
						}
				);
		        
		        
		    }*/
		
	
	
};
	}



]);


