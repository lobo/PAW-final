define(['clickerQuest','services/AccountService'], function(clickerquest) {

  'use strict';
  clickerquest.service('MarketService', function($http, AccountService) {

    this.url = "http://localhost:8080/api/v1/market/";
    this.url = "./api/v1/market/";   // FOR DEPLOY

    this.getPrices = function () {
      return $http.get(this.url + "prices")
    };

    this.buy = function (resId, amount) {
      const body = JSON.stringify({resource_type: resId, amount: amount});
      return AccountService.authenticated('POST', this.url + "purchase", body);
    };

    this.sell = function (resId, amount) {
      const body = JSON.stringify({resource_type: resId, amount: amount});
      return AccountService.authenticated('POST', this.url + "sell", body);
    };

  });

});
