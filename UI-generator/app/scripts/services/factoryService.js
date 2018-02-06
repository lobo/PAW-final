define(['clickerQuest','services/AccountService'], function(clickerquest) {

    'use strict';
    clickerquest.service('factoryService', function($http, AccountService) {

      this.url = "http://localhost:8080/api/v1/factories/";
      this.url = "./api/v1/factories/";  // FOR DEPLOY

      this.getAllFactories = function () {
        return $http.get(this.url + "all");
      };

      this.getFactory = function (factoryId) {
        return $http.get(this.url + factoryId);
      };

      this.getFactoryRecipe = function (factoryId) {
        return $http.get(this.url + factoryId + "/recipe");
      };

      this.buyFactory = function (factoryId, amount) {
        const body = JSON.stringify({id: factoryId, amount: amount});
        return AccountService.authenticated('POST', this.url + "purchase", body)
      };

      this.upgradeFactory = function (factId) {
        const body = JSON.stringify({id: factId});
        return AccountService.authenticated('POST', this.url + "upgrade", body)
      };

      this.upgradeFactory = function (factoryId) {
        const body = JSON.stringify({id: factoryId});
        return AccountService.authenticated('POST', this.url + "upgrade", body)
      }

    });

});
