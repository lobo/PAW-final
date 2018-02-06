function updateClan(clan, newClan) {
  clan.id = newClan.id;
  clan.name = newClan.name;
  clan.image = newClan.image;
  clan.score = newClan.score;
  clan.wins = newClan.wins;
  clan.battles = newClan.battles;
  clan.battle_url = newClan.battle_url;
}

define(['clickerQuest', 'services/UserService','services/ClanService'], function(clickerQuest) {

  'use strict';
  clickerQuest.controller('clanCtrl', function($scope, $rootScope, $routeParams, $location, ClanService) {
    $scope.formatNumber = formatNumber;
    if($routeParams.id != null) {
      $scope.clanID = $routeParams.id;
      $rootScope.clanID = $routeParams.id;
    } else {
      $scope.clanID = $rootScope.user.clan_id;
      $rootScope.clanID = $rootScope.user.clan_id;
    }

    $scope.clan = {
      id: null,
      name: null,
      image: null, // TODO: put default profile image path
      score: null,
      rank: null,
      wins: null,
      battles: null
    };

    ClanService.getClan($scope.clanID).then(
      function(response) {
        updateClan($scope.clan, response.data) },
      function(error) {
        $location.path('/404');
        $location.replace();
      }
    );

    ClanService.getClanRank($scope.clanID).then(
      function (response) {
        $scope.clan.rank = response.data.rank;
      }
    );

    $scope.battle = null;

    ClanService.getClanBattle($scope.clanID).then(
      function (response) {
        $scope.battle = {
          versus: {
            id: null,
            name: null,
            image: null,
            score: null
          },
          deltaScore: null,
          versusDeltaScore: null
        };
        $scope.battle.deltaScore = response.data.delta_score;
        $scope.battle.versusDeltaScore = response.data.opponent_delta_score;
        $scope.battle.versus.id = response.data.oppponent_id;
        ClanService.getClan($scope.battle.versus.id).then(
          function(response) { updateClan($scope.battle.versus, response.data) }
        );
      });


    $scope.members = [ ];

    ClanService.getClanUsers($scope.clanID).then(
      function(response) {
        var users = response.data.users;
        users.forEach(function (u) {
          $scope.members.push({id: u.id, name: u.username, image: u.profile_image_url ,score: u.score});
        });

        $scope.members.sort(function (u1, u2) {
          return u2.score - u1.score;
        })
      }
    )

  });

});
