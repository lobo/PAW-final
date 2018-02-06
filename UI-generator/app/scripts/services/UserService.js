define(['clickerQuest'], function(clickerquest) {

    'use strict';
    clickerquest.service('UserService', function($http) {

      this.url = "http://localhost:8080/api/v1/users/";
      this.url = "./api/v1/users/";  // FOR DEPLOY

      this.getUser = function(userId) {
        return $http.get(this.url + userId);
      };

      this.getUserByName = function(username) {
        return $http.get(this.url + "username/" + username);
      };

      this.getAllUsers = function() {
        return $http.get(this.url + "all");
      };

      this.getAllUsersByPage = function(page, pageSize) {
        return $http.get(this.url + "all", {
          params: {
            page: page,
            pageSize: pageSize
          }
        });
      };

      this.getWealth = function(userId) {
        return $http.get(this.url + userId + "/wealth");
      }

      this.getFactories = function(userId) {
        return $http.get(this.url + userId + "/factories");
      }

      this.getFactory = function(userId, factoryID) {
        return $http.get(this.url + userId + "/factories/" + factoryID);
      }

      this.getFactoryRecipe = function(userId, factoryID) {
        return $http.get(this.url + userId + "/factories/" + factoryID + "/recipe");
      }

      this.getFactoryUpgrade = function(userId, factoryID) {
        return $http.get(this.url + userId + "/factories/" + factoryID + "/upgrade");
      }

      this.getFactoryBuyLimits = function(userId, factoryID) {
        return $http.get(this.url + userId + "/factories/" + factoryID + "/buyLimits");
      }

      this.getRank = function(userId) {
        return $http.get(this.url + userId + "/rank");
      }

    });

});
