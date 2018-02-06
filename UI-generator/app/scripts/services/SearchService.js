define(['clickerQuest'], function(clickerquest) {

  'use strict';
  clickerquest.service('SearchService', function($http) {

    this.url = "http://localhost:8080/api/v1/search/";
    this.url = "./api/v1/search/";   // FOR DEPLOY

    this.searchQuery = function(string) {
      return $http.get(this.url + "?query=" + string);
    };

  });

});
