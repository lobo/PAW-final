define(['clickerQuest'], function(clickerquest) {

    'use strict';
    clickerquest.service('AccountService', function($http, $q, $location) {

      this.url = "http://localhost:8080/api/v1/accounts/";
      this.url = "./api/v1/accounts/";  //FOR DEPLOY

      this.key = "token-clickerQuest";

      this.saveToken = function(token, remeberMe){
        localStorage.getItem(this.key);
        sessionStorage.getItem(this.key);
        if(remeberMe) {
          localStorage.setItem(this.key, token)
        } else {
          sessionStorage.setItem(this.key,token)
        }
      };

      this.getToken = function () {
        if(localStorage.getItem(this.key)) {
          return localStorage.getItem(this.key)
        } else {
          return sessionStorage.getItem(this.key)
        }
      };

      this.eraseToken = function () {
        localStorage.removeItem(this.key);
        sessionStorage.removeItem(this.key);
      };

      this.authenticated = function(method, url, body) {
        return $http({
          method: method,
          url: url,
          headers: {'Authorization': this.getToken()},
          data: body
        })
      };

      this.createUser = function (username, password) {
        const body = JSON.stringify({username: username, password: password});
        return $http.post(this.url + "create", body);
      };

      this.getUser = function () {
        return this.authenticated('GET', this.url + "user")
      };

      this.formFormat = function(obj){
        var str = [];
        for(var p in obj)
          str.push(encodeURIComponent(p) + "=" + encodeURIComponent(obj[p]));
        return str.join("&");
      };

      this.login = function (username, password, remeberMe) {
        var that = this;
        return $http({
          method: 'POST',
          url: this.url + "login",
          headers: {'Content-Type': 'application/x-www-form-urlencoded'},
          data: this.formFormat({username: username, password: password})
        })
          .then(function(response) {
            that.eraseToken();
            that.saveToken(response.headers("authorization"), remeberMe);
            return $q.resolve(response);
          });
      };

      this.logout = function () {
        this.eraseToken();
      }
    });

});
