define(['clickerQuest','services/AccountService'], function(clickerquest) {

  'use strict';
  clickerquest.service('ClanService', function($http, AccountService) {

    this.url = "http://localhost:8080/api/v1/clans/";
    this.url = "./api/v1/clans/";  // FOR DEPLOY

    this.getAllClans = function() {
      return $http.get(this.url + "all");
    };

    this.getAllClansByPage = function(page, pageSize) {
      return $http.get(this.url + "all?page=" + page + "&pageSize=" + pageSize);
    };

    this.getClan = function(clanID) {
      return $http.get(this.url + clanID);
    };

    this.getClanUsers = function(clanID) {
      return $http.get(this.url + clanID + "/users");
    };

    this.getClanBattle = function(clanID) {
      return $http.get(this.url + clanID + "/battle");
    };

    this.getClanRank = function(clanID) {
      return $http.get(this.url + clanID + "/rank");
    };

    this.leaveClan = function() {
      return AccountService.authenticated('delete', this.url + "leave");
    };


    this.getClan = function(clanID) {
      return $http.get(this.url + clanID);
    };

    this.createClan = function(name) {
      const body = JSON.stringify({name: name});
      return AccountService.authenticated('POST', this.url + "create", body);
    };

    this.joinClan = function(clanID) {
      const body = JSON.stringify({id: clanID});
      return AccountService.authenticated('POST', this.url + "join", body);
    };
  });

});
