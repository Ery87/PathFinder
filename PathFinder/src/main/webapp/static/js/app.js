'use strict';
	
	var App=angular.module('myApp',['ngRoute']);
	App.config(['$routeProvider', '$compileProvider', function($routeProvider, $compileProvider) {
		$compileProvider.aHrefSanitizationWhitelist(/^\s*(https?|file|ftp|blob):|data:image\//);
	    $compileProvider.imgSrcSanitizationWhitelist(/^\s*(https?|ftp|mailto|chrome-extension):/);

		$routeProvider
		
			.when('/',{
					controller:'LoginController',
					templateUrl: 'WEB-INF/views/UserManagement.jsp'
		})
		
			
		.when('/registration',{
					controller:'EvalController',
					templateUrl: 'WEB-INF/views/registration.jsp'
		})

		
			.otherwise({redirectTo:'/'});
		}]);
	
