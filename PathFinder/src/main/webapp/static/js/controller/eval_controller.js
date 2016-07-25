'user strict';
App.controller('EvalController',['EvalService','$window','$scope',function (EvalService,$window,$scope){
	var id;   
         
        
       window.onload=function(){
    	   self.evaluation();
       }  
         
       self.evaluation=function(){
    	    EvalService.eval(12)
		              .then(
		            		  function(d){
		            			 console.log(d)
		            			  self.id=d;
		            			  
		            			
		            			
		            		  },
				              function(errResponse){
					               console.error('Error while creating User.');

				              }	
                 );
       };

       
       
       
       
}]);

App.directive('fileModel', ['$parse', function ($parse) {

    return {
        restrict: 'A',
        link: function(scope, element, attrs) {
            var model = $parse(attrs.fileModel);
            var modelSetter = model.assign;
            
            element.bind('change', function(){
                scope.$apply(function(){
                    modelSetter(scope, element[0].files[0]);
                });
            });
        }
    };

}]);
