define(['clickerQuest', 'services/UserService', 'services/ClanService'], function(clickerQuest) {

    'use strict';
    clickerQuest.controller('profileCtrl', function($scope, $routeParams, $rootScope, $location, UserService, ClanService) {
      $scope.factImg = [];
      $scope.resImg = [];
      if($routeParams.id != null) {
        $scope.userID = $routeParams.id;
      } else {
        $scope.userID = $rootScope.user.id
      }

      $scope.formatNumber = formatNumber;

      //TODO check if this has to be done everywhere
      $scope.$watch($rootScope.user.id, function() {
          if($routeParams.id == null) {
            $scope.userID = $rootScope.user.id
          }
        }
      );

      UserService.getUser($scope.userID).then(
        function (response) {
          $scope.user = response.data;

          ClanService.getClan(response.data.clan_id).then(
            function (response) {
              $scope.user.clan = response.data;
            }
          );

          UserService.getRank($scope.userID).then(
            function (response) {
              console.log(response.data)
              $scope.user.ranking = response.data.rank;
            }
          )
        },
        function(error) {
          $location.path('/404');
          $location.replace();
        }
      );


      UserService.getFactories($scope.userID).then(
        function (response) {
          $scope.factories = response.data.factories;
          $scope.factories.sort(function (f1, f2) {
            return f2.type_id - f1.type_id;
          })
        }
      );

      UserService.getWealth($scope.userID).then(
        function (response) {
          $scope.resources = response.data.resources;
          $scope.resources.sort(function(r1,r2){
            return r2.id - r1.id;
          })
        }
      );

    });

});
