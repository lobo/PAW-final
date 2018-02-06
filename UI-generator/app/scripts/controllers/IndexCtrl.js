var clanImageSrc = "http://simpleicon.com/wp-content/uploads/multy-user.png";
var userImageSrc = "https://www.bosserpropiedades.com/wp-content/uploads/2016/03/foto-perfil-2.png";
var autocomplete_id;

var translations_en = {
  HOME: {
    USERNAME: 'Username',
    PASSWORD: 'Password',
    REMEMBER: 'Remember Me',
    REGISTER: 'Don\'t have an account? Register HERE',
    ENTERUSERNAME: 'Enter a username',
    ENTERPASSWORD: 'Enter a password',
    INCORRECTPASSWORD: 'Incorrect user or password'
  },
  INDEX: {
    CLANRANKING: 'Clan Ranking',
    USERRANKING: 'User Ranking',
    LEAVECLAN: 'Leave Clan',
    JOINCLAN: 'Join Clan',
    CREATECLAN: 'Create Clan',
    MYCLAN: 'My Clan',
    LOGOUT: 'Logout',
    SELECTNAME: 'Name',
    SEARCHBAR: 'Search users or clans',
    CLANEXISTS: 'Clan already exists',
  },
  GAME: {
    SCORE: 'Score',
    RANK: 'Rank',
    RESOURCES: 'Resources',
    CONSUMPTION: 'Consumption',
    AMOUNT: 'Amount',
    RECIPE: 'Recipe',
    PRODUCTION: 'Production',
    FACTORIES: 'Factories',
    FACTORY: 'Factory',
    UPGRADE: 'Upgrade',
    TOUPGRADE: 'Upgrade',
    MARKET: 'Market',
    MARKETWELCOME: 'Welcome to the market!',
    SELECTRESOURCE: 'Select resource',
    SELECTUNIT: 'Select unit',
    COST: 'Cost',
    PRICERED:'Decrement cost',
    INPUTRED: 'Decrement consumption',
    OUTPUTINC: 'Increment production',
    BUYSUCCES: 'Buy successful',
    BUYFAIL: 'Buy failed',
    SELLSUCCES: 'Sell successful',
    SELLFAIL: 'Sell failed',
    SELLTOOMUCH: 'Sell failed: Not enough resources',
    BUYTOOMUCH: 'Buy failed: Not enough money',
    BUYING: 'Making purchase...',

  },
  PROFILE: {
    CLAN: 'Clan',
    UNLOCKEDFACTORIES: 'Unlocked Factories',
    RESOURCES: 'Resources',
    SCORE: 'Score',
    RANK: 'Rank'
  },
  USERRANKING: {
    USER: 'User',
    PREV: 'Previous',
    NEXT: 'Next'
  },
  REGISTER: {
    REGISTER: 'REGISTER',
    REPEATPASS: 'Repeat Password',
    BACK: 'BACK',
    USERNAMELENGTH: 'Username must be between 4 and 30 characters',
    PASSWORDLENGTH: 'Password must be between 4 and 30 characters',
    PASSWORDMATCH: 'Passwords must match',
    ALREADYEXISTS: 'User already exists'
  },
  CLAN: {
    BATTLESWON: 'Battles Won',
    TOTALBATTLES: 'Total Battles',
    NOBATTLES: 'No battles for today',
    ABANDON: 'The opposite has abandoned',
    POINTSTODAY: 'Points Today',
    MEMBERS: 'Clan Members',
    BATTLEOFTHEDAY: 'Battle of the Day'
  },
  ERROR: {
    LIAMNEESONMSG: "I don't know who you are. I don't know what you want. If you are looking for this page, I can tell you, I don't have it.\n" +
    "But what I do have are a very particular set of error pages. Error pages I have acquired over a very long career working in the shadows. Error pages that make me a nightmare for people like you.\n" +
    "If you return to the previous page, that'll be it. I will not look for you, I will not pursue you. But if you don't, I will look for you, I will find you, and I will kill you.\n"
  }
};

var translations_es = {
  HOME: {
    USERNAME: 'Usuario',
    PASSWORD: 'Contraseña',
    REMEMBER: 'Recuérdame',
    REGISTER: '¿No tienes cuenta? Regístrate AQUI',
    ENTERUSERNAME: 'Ingresa un usuario',
    ENTERPASSWORD: 'Ingresa una contraseña',
    INCORRECTPASSWORD: 'Usuario o contraseña incorrecta',
    CLANEXISTS: 'El clan ya existe'
  },
  INDEX: {
    CLANRANKING: 'Ranking de Clanes',
    USERRANKING: 'Ranking de Usuarios',
    LEAVECLAN: 'Salir del Clan',
    JOINCLAN: 'Unirse al Clan',
    CREATECLAN: 'Crear Clan',
    MYCLAN: 'Mi Clan',
    LOGOUT: 'Cerrar Sesión',
    SELECTNAME: 'Nombre',
    SEARCHBAR: 'Busca usuarios o clanes'
  },
  GAME: {
    SCORE: 'Puntaje',
    RANK: 'Ranking',
    RESOURCES: 'Recursos',
    CONSUMPTION: 'Consumo',
    AMOUNT: 'Cantidad',
    RECIPE: 'Receta',
    PRODUCTION: 'Producción',
    FACTORIES: 'Fábricas',
    FACTORY: 'Fábrica',
    UPGRADE: 'Mejora',
    TOUPGRADE: 'Mejorar',
    MARKET: 'Mercado',
    MARKETWELCOME: '¡Bienvenido al mercado!',
    SELECTRESOURCE: 'Elegir recurso',
    SELECTUNIT: 'Elegir unidad',
    COST: 'Costo',
    PRICERED:'Decrementar costo',
    INPUTRED: 'Decrementar consumo',
    OUTPUTINC: 'Incrementar producción',
    BUYSUCCES: 'Compra exitosa',
    BUYFAIL: 'Compra fallida',
    SELLSUCCES: 'Venta exitosa',
    SELLFAIL: 'Venta fallida',
    SELLTOOMUCH: 'Venta fallida: No tienes suficientes recursos',
    BUYTOOMUCH: 'Compra fallida: No tienes suficiente dinero',
    BUYING: 'Efectuando compra...',
  },
  PROFILE: {
    CLAN: 'Clan',
    UNLOCKEDFACTORIES: 'Fábricas desbloqueadas',
    RESOURCES: 'Recursos',
    SCORE: 'Puntaje',
    RANK: 'Ranking'
  },
  USERRANKING: {
    USER: 'Usuario',
    PREV: 'Anterior',
    NEXT: 'Siguiente'
  },
  REGISTER: {
    REGISTER: 'REGISTRO',
    REPEATPASS: 'Repetir Contraseña',
    BACK: 'ATRÁS',
    USERNAMELENGTH: 'El usuario debe tener entre 4 y 30 caracteres',
    PASSWORDLENGTH: 'La contraseña debe tener entre 4 y 30 caracteres',
    PASSWORDMATCH: 'Las contraseñas no coinciden',
    ALREADYEXISTS: 'El usuario ya exise'
  },
  CLAN: {
    BATTLESWON: 'Batallas Ganadas',
    TOTALBATTLES: 'Batallas Totales',
    NOBATTLES: 'No hay batallas hoy',
    ABANDON: 'El contrincante abandonó la batalla',
    POINTSTODAY: 'Puntos hoy',
    MEMBERS: 'Miembros del Clan',
    BATTLEOFTHEDAY: 'Batalla del Día'
  },
  ERROR: {
    LIAMNEESONMSG: "No sé quién eres. No sé qué es lo que quieres. Si lo que quieres es esta página, desde ahora te lo digo, no la tengo.\n" +
    "Pero lo que sí tengo es un conjunto de errores muy particulares. Errores que he adquirido a lo largo de una prolongada carrera trabajando bajo las sombras. Errores que me hacen una pesadilla para gente como tú.\n" +
    "Si vuelves a la página anterior, ahí terminará todo. No te buscaré, no te perseguiré. Pero si no lo haces, te buscaré, te encontraré y te mataré.\n"
  }
};

function getBrowserLanguage() {
  var preferredLangKey = navigator.language.substring(0, 2);
  if (['en', 'es'].includes(preferredLangKey)) {
    return preferredLangKey;
  } else {
    return 'en';
  }
}

'use strict';
define(['clickerQuest','services/AccountService','services/SearchService','services/UserService','services/ClanService'], function(clickerQuest) {

  clickerQuest.config(['$translateProvider', function ($translateProvider) {
    // add translation table
    $translateProvider.translations('en', translations_en);
    $translateProvider.translations('es', translations_es);
    $translateProvider.determinePreferredLanguage(getBrowserLanguage());
  }]);

  clickerQuest.controller('IndexCtrl', function($scope, $translate, $location, $rootScope, SearchService, UserService, ClanService, AccountService) {

    if(!AccountService.getToken()) {
      $location.url("/");
    }


    $scope.snackbarMsg = ""

    var translations = translations_en;
    if (getBrowserLanguage() == "es") {
      var translations = translations_es;
    }
      //HOME
      $scope.home_username = translations["HOME"]["USERNAME"];
      $scope.home_password = translations["HOME"]["PASSWORD"];
      $scope.home_remember = translations["HOME"]["REMEMBER"];
      $scope.home_register = translations["HOME"]["REGISTER"];
      $scope.home_enterUsername = translations["HOME"]["ENTERUSERNAME"];
      $scope.home_enterPassword = translations["HOME"]["ENTERPASSWORD"];
      $scope.home_incorrectPassword = translations["HOME"]["INCORRECTPASSWORD"];
      //INDEX
      $scope.index_clanRanking = translations["INDEX"]["CLANRANKING"];
      $scope.index_userRanking = translations["INDEX"]["USERRANKING"];
      $scope.index_leaveClan = translations["INDEX"]["LEAVECLAN"];
      $scope.index_joinClan = translations["INDEX"]["JOINCLAN"];
      $scope.index_createClan = translations["INDEX"]["CREATECLAN"];
      $scope.index_myClan = translations["INDEX"]["MYCLAN"];
      $scope.index_logout = translations["INDEX"]["LOGOUT"];
      $scope.index_selectName = translations["INDEX"]["SELECTNAME"];
      $scope.index_searchBar = translations["INDEX"]["SEARCHBAR"];
      $scope.index_clanExists = translations["INDEX"]["CLANEXISTS"];
      //GAME
      $scope.game_score = translations["GAME"]["SCORE"];
      $scope.game_rank = translations["GAME"]["RANK"];
      $scope.game_resources = translations["GAME"]["RESOURCES"];
      $scope.game_consumption = translations["GAME"]["CONSUMPTION"];
      $scope.game_amount = translations["GAME"]["AMOUNT"];
      $scope.game_recipe = translations["GAME"]["RECIPE"];
      $scope.game_production = translations["GAME"]["PRODUCTION"];
      $scope.game_factories = translations["GAME"]["FACTORIES"];
      $scope.game_factory = translations["GAME"]["FACTORY"];
      $scope.game_upgrade = translations["GAME"]["UPGRADE"];
      $scope.game_toUpgrade = translations["GAME"]["TOUPGRADE"];
      $scope.game_market = translations["GAME"]["MARKET"];
      $scope.game_marketWelcome = translations["GAME"]["MARKETWELECOME"];
      $scope.game_selectResource = translations["GAME"]["SELECTRESOURCE"];
      $scope.game_selectUnit = translations["GAME"]["SELECTUNIT"];
      $scope.game_cost = translations["GAME"]["COST"];
      $scope.game_priceRed = translations["GAME"]["PRICERED"];
      $scope.game_inputRed = translations["GAME"]["INPUTRED"];
      $scope.game_outputInc = translations["GAME"]["OUTPUTINC"];
      $scope.game_buySuccess = translations["GAME"]["BUYSUCCES"];
      $scope.game_buyFail = translations["GAME"]["BUYFAIL"];
      $scope.game_sellSuccess = translations["GAME"]["SELLSUCCES"];
      $scope.game_sellFail = translations["GAME"]["SELLFAIL"];
      $scope.game_sellTooMuch = translations["GAME"]["SELLTOOMUCH"];
      $scope.game_buyTooMuch = translations["GAME"]["BUYTOOMUCH"];
      $scope.game_buying = translations["GAME"]["BUYING"];
      //PROFILE
      $scope.profile_clan = translations["PROFILE"]["CLAN"];
      $scope.profile_unlockedFactories = translations["PROFILE"]["UNLOCKEDFACTORIES"];
      $scope.profile_resources = translations["PROFILE"]["RESOURCES"];
      $scope.profile_score = translations["PROFILE"]["SCORE"];
      $scope.profile_rank = translations["PROFILE"]["RANK"];
      //USERRANKING
      $scope.userRanking_user = translations["USERRANKING"]["USER"];
      $scope.userRanking_prev = translations["USERRANKING"]["PREV"];
      $scope.userRanking_next = translations["USERRANKING"]["NEXT"];
      //REGISTER
      $scope.register_register = translations["REGISTER"]["REGISTER"];
      $scope.register_repeatPass = translations["REGISTER"]["REPEATPASS"];
      $scope.register_back = translations["REGISTER"]["BACK"];
      $scope.register_usernameLength = translations["REGISTER"]["USERNAMELENGTH"];
      $scope.register_passwordLength = translations["REGISTER"]["PASSWORDLENGTH"];
      $scope.register_passwordMatch = translations["REGISTER"]["PASSWORDMATCH"];
      $scope.register_alreadyExists = translations["REGISTER"]["ALREADYEXISTS"];
      //CLAN
      $scope.clan_battlesWon = translations["CLAN"]["BATTLESWON"];
      $scope.clan_totalBattles = translations["CLAN"]["TOTALBATTLES"];
      $scope.clan_noBattles = translations["CLAN"]["NOBATTLES"];
      $scope.clan_abandon = translations["CLAN"]["ABANDON"];
      $scope.clan_pointsToday = translations["CLAN"]["POINTSTODAY"];
      $scope.clan_members = translations["CLAN"]["MEMBERS"];
      $scope.clan_battleOfTheDay = translations["CLAN"]["BATTLEOFTHEDAY"];
      $scope.error_liamNeesonMsg= translations["ERROR"]["LIAMNEESONMSG"];

    // Sorted by ID
    $scope.factoriesName = [];
    $scope.resourcesName = [];
    if (navigator.language.substring(0, 2) == 'es') {
      // ESPAÑOL
      $scope.factoriesName.push("Bolsa de Acciones");
      $scope.factoriesName.push("Reclutadora de Personas");
      $scope.factoriesName.push("Recolector de Basura");
      $scope.factoriesName.push("Separadora de Metales");
      $scope.factoriesName.push("Trituradora de Caucho");
      $scope.factoriesName.push("Fábrica de Cables");
      $scope.factoriesName.push("Caldera");
      $scope.factoriesName.push("Fábrica de Circuitos");

      $scope.resourcesName.push("Energía");
      $scope.resourcesName.push("Plástico");
      $scope.resourcesName.push("Oro");
      $scope.resourcesName.push("Dinero");
      $scope.resourcesName.push("Personas");
      $scope.resourcesName.push("Acero");
      $scope.resourcesName.push("Ruedas");
      $scope.resourcesName.push("Caucho");
      $scope.resourcesName.push("Chatarra");
      $scope.resourcesName.push("Cobre");
      $scope.resourcesName.push("Cables de Cobre");
      $scope.resourcesName.push("Cartón");
      $scope.resourcesName.push("Circuitos");
    } else {
      // DEFAULT: ENGLISH
      $scope.factoriesName.push("Stock Investor");
      $scope.factoriesName.push("People Recruiter");
      $scope.factoriesName.push("Junk Collector");
      $scope.factoriesName.push("Metal Separator");
      $scope.factoriesName.push("Rubber Shredder");
      $scope.factoriesName.push("Cable Maker");
      $scope.factoriesName.push("Boiler");
      $scope.factoriesName.push("Circuit Maker");

      $scope.resourcesName.push("Power");
      $scope.resourcesName.push("Plastic");
      $scope.resourcesName.push("Gold");
      $scope.resourcesName.push("Money");
      $scope.resourcesName.push("People");
      $scope.resourcesName.push("Iron");
      $scope.resourcesName.push("Tires");
      $scope.resourcesName.push("Rubber");
      $scope.resourcesName.push("Metal Scrap");
      $scope.resourcesName.push("Copper");
      $scope.resourcesName.push("Copper Cables");
      $scope.resourcesName.push("Cardboard");
      $scope.resourcesName.push("Circuits");
    }

    $scope.showCreateClan = false;
    $(document).click(function(event) {
      if($(event.target)[0].id == "createClanBtn") {
        $scope.showCreateClan = true;
      }
    });
    $('#clanModal').on('keydown', function(e) {
      if (e.which == 13) {
        $('#createClanSend').click()
      }
    });
    $scope.searchField = "";
    $scope.location = $location;

    $scope.buttonImg = {};

    if (getBrowserLanguage() == "es") {
      $scope.buttonsFolder = "buttons_es";
      $scope.createButton = "./images/buttons_es/create.png";
    } else {
      $scope.createButton = "./images/buttons/create.png";
      $scope.buttonsFolder = "buttons";
    }

    $scope.isAnotherClan = function(){
      return /clan\/\d+/.test($location.path());
    };

    $scope.updateUser = function (redirect) {
      if(!$rootScope.user || !$rootScope.user.id) {
        $location.url("/")
      } else {
        UserService.getUser($rootScope.user.id).then(function (response) {
          $rootScope.user = response.data;
          if (redirect) {
            $location.url(redirect);
          }
        })
      }
    };

    $scope.updateUser();


    $scope.createClanError = "";
    $scope.createClan = function(name){
      ClanService.createClan(name).then(
        function(response){
          $scope.createClanError = "";
          $scope.updateUser('clan');
          $scope.showCreateClan = false;
        },
        function(response){
          $scope.createClanError = $scope.index_clanExists;
        });
    };

    $scope.joinClan = function(clanId) {
      ClanService.joinClan(clanId).then(function(response) {
        $scope.updateUser('clan');

      });
    };

    $scope.leaveClan = function() {
      ClanService.leaveClan().then(function(response) {
        $scope.updateUser('/game');
      });
    };



    $scope.updateSearch = function (event) {
      SearchService.searchQuery($scope.searchField).then(
        function (response) {
          $scope.autocomplete = response.data;
          searchCommunity();
        }
      );
    }

    $scope.logout = function(){
      AccountService.logout();
      $location.url("/");
    }

    $scope.searchCommunity = function () {
      //TODO
    }

    $scope.autoCompleteOptions = {
      minimumChars: 1,
      dropdownWidth: '400px',
      selectedTextAttr: 'name',
      data: function (searchText) {
        return SearchService.searchQuery(searchText).then(function(response){
          var searches = response.data.results
          for (var i = 0; i < searches.length; i++) {
            if (searches[i].type == 'clan')
              searches[i].image = "./images/clanImage.png"
            if (searches[i].type == 'user')
              searches[i].image = "./images/userImage.png"

          }
          return searches
        })
      },
      itemSelected: function (selected) {
        var object = selected.item
        if(object.type == "user") {
          $scope.updateUser("user/" + object.id);
        }

        if(object.type == "clan") {
          if ($scope.user) {
            if (object.id == $scope.user.clan_id) {
              $scope.updateUser("clan");
            } else {
              $scope.updateUser("clan/" + object.id);
            }
          }
        }
      },
      itemTemplateUrl: 'views/autocompleteEntry.html'
    }

  });


});


