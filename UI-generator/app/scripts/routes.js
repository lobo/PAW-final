'use strict';

define([], function() {
    return {
        defaultRoutePath: '/game',
        routes: {
            '/': {
                templateUrl: './views/home.html',
                controller: 'HomeCtrl'
            },
            '/game': {
                templateUrl: './views/game/GameCtrl.html',
                controller: 'GameCtrl'
            },
            '/profile': {
                templateUrl: './views/profile/profileCtrl.html',
                controller: 'profileCtrl'
            },
            '/user/:id': {
              templateUrl: './views/profile/profileCtrl.html',
              controller: 'profileCtrl'
            },
            '/clan': {
                templateUrl: './views/clan/clanCtrl.html',
                controller: 'clanCtrl'
            },
            '/clan/:id': {
              templateUrl: './views/clan/clanCtrl.html',
              controller: 'clanCtrl'
            },
            '/userRanking': {
                templateUrl: './views/userRanking/userRanking.html',
                controller: 'userRanking'
            },
            '/clanRanking': {
                templateUrl: './views/clanRanking/clanRanking.html',
                controller: 'clanRanking'
            },
            '/register': {
                templateUrl: './views/register/RegisterCtrl.html',
                controller: 'RegisterCtrl'
            },
            '/404': {
                templateUrl: '/views/404/404.html',
                controller: '404'
            }
            /* ===== yeoman hook ===== */
            /* Do not remove these commented lines! Needed for auto-generation */
        }
    };
});
