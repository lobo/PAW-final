define(['clickerQuest', 'services/AccountService'], function(clickerquest) {

  'use strict';
  clickerquest.controller('RegisterCtrl', function($scope, $rootScope, $location, AccountService) {
    if(AccountService.getToken()) {
      $location.url("game")
    }

    $scope.username = null;
    $scope.password = null;
    $scope.password2 = null;

    $scope.register = function () {
      $scope.usernameError = "";
      $scope.passwordError = "";
      $scope.password2Error = "";
      var error = false;

      if (!$scope.username) {
        $scope.usernameError = $scope.home_enterUsername;
        error = true;
      } else if ($scope.username.length < 4 || $scope.username.length > 30) {
        $scope.usernameError = $scope.register_usernameLength;
        error = true;
      }

      if (!$scope.password) {
        $scope.passwordError = $scope.home_enterPassword;
        error = true;
      } else if ($scope.password.length < 4 || $scope.password.length > 30) {
        $scope.passwordError = $scope.register_passwordLength;
        error = true;
      }

      if ($scope.password != $scope.password2) {
        $scope.password2Error = $scope.register_passwordMatch;
        error = true;
      }

      if (!error) {
        AccountService.createUser($scope.username, $scope.password).then(
          function (response) {
            $rootScope.user = undefined;
            AccountService.login($scope.username, $scope.password, false).then(
              function (response){
                $rootScope.user = response.data;
                $location.url("game")
              }
            )
          },
          function (response) {
            $scope.usernameError = $scope.register_alreadyExists;
          }
        )
      }
    }


    $('#username').on('keydown', function(e) {
      if (e.which == 13) {
        $scope.register();
      }
    });


    $('#password').on('keydown', function(e) {
      if (e.which == 13) {
        $scope.register();
      }
    });

    $('#password2').on('keydown', function(e) {
      if (e.which == 13) {
        $scope.register();
      }
    });

  });

});
