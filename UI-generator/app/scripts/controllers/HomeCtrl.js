'use strict';
define(['clickerQuest','services/UserService','services/AccountService'], function(clickerQuest) {

  clickerQuest.controller('HomeCtrl', function($scope, $translate, AccountService, UserService, $rootScope, $location) {

    if(AccountService.getToken()) {
      $location.url("game")
    }
    $scope.rememberMe = false;
    $scope.login = function(){
      $scope.usernameError = "";
      $scope.passwordError = "";

      var error = false;
      if(!$scope.username){
        $scope.usernameError = $scope.home_enterUsername;
        error = true;
      }

      if (!$scope.password) {
        $scope.passwordError = $scope.home_enterPassword;
        error = true;
      }

      if (!error) {
        AccountService.login($scope.username, $scope.password, $scope.rememberMe).then(
          function (response) {
            UserService.getUserByName($scope.username).then(
              function (response) {
                $rootScope.user = response.data;
                $location.url("game")
              }
            );
          },
          function (response) {
            $scope.passwordError = $scope.home_incorrectPassword;
          })
      }

    }

    $('#usernameInput').on('keydown', function(e) {
      if (e.which == 13) {
        $scope.login();
      }
    });

    $('#passwordInput').on('keydown', function(e) {
      if (e.which == 13) {
        $scope.login();
      }
    });

  });
});


