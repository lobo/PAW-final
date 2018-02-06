define(['clickerQuest', 'services/ClanService'], function(clickerQuest) {

    'use strict';
    clickerQuest.controller('clanRanking', function($scope, ClanService) {
      $scope.formatNumber = formatNumber;

      $scope.page = parseInt(getParameterByName("page", window.location.href));
      if (!$scope.page) $scope.page = 1;

      $scope.itemsPerPage = 10;

      ClanService.getAllClansByPage($scope.page, $scope.itemsPerPage).then(
        function (response) {
          $scope.clans = response.data.elements;
          $scope.totalPages = response.data.total_pages;
        }
      );


    });

});
