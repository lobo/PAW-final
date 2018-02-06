'use strict';
define([
  'routes',
	'services/dependencyResolverFor',
	'i18n/i18nLoader!',
	'angular',
	'angular-route',
	'bootstrap',
	'angular-translate',
  'angular-sanitize',
  'angular-auto-complete',
  'ngModal'],
	function(config, dependencyResolverFor, i18n) {
		var clickerQuest = angular.module('clickerQuest', [
			'ngRoute',
			'pascalprecht.translate',
      'autoCompleteModule',
      'ngSanitize',
      'ngModal'
    ]);
		clickerQuest
			.config(
				['$routeProvider',
				'$controllerProvider',
				'$compileProvider',
				'$filterProvider',
				'$provide',
				'$translateProvider',
				function($routeProvider, $controllerProvider, $compileProvider, $filterProvider, $provide, $translateProvider) {

					clickerQuest.controller = $controllerProvider.register;
					clickerQuest.directive = $compileProvider.directive;
					clickerQuest.filter = $filterProvider.register;
					clickerQuest.factory = $provide.factory;
					clickerQuest.service = $provide.service;

					if (config.routes !== undefined) {
						angular.forEach(config.routes, function(route, path) {
							$routeProvider.when(path, {templateUrl: route.templateUrl, resolve: dependencyResolverFor(['controllers/' + route.controller]), controller: route.controller, gaPageTitle: route.gaPageTitle});
						});
					}
					if (config.defaultRoutePath !== undefined) {
						$routeProvider.otherwise({redirectTo: config.defaultRoutePath});
					}
				}]);
		return clickerQuest;
	}
);
