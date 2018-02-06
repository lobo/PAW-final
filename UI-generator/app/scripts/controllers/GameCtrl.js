function initializeViewModel($scope) {

  $scope.formatNumber = formatNumber;
  $scope.resources = {};
  $scope.marketPrices = [];
  $scope.factories = [];
  $scope.factImg = [];
  $scope.resImg = [];

  $scope.user = {
    username: null,
    score: null,
    image: null,  // TODO: DEFAULT IMAGE
    ranking: null,
    clan: {
      id: null,
      name: null
    }
  };

  $scope.market = { buy: {resourceID: null, amount: NaN, unit: NaN, price: NaN},
                    sell: {resourceID: null, amount: NaN, unit: NaN, price: NaN}
                  };

  $scope.canBuy = function(factId, amount) {
    return $scope.factories[factId].buyLimits.max >= amount
  };

  $scope.canUpgrade = function(factId) {
    var moneyID = 3;
    var money = $scope.resources[moneyID];
    if(money){
      return $scope.factories[factId].upgrade.cost <= $scope.resources[moneyID].storage
    } else {
      return false;
    }
  };
}

function marketResourceNameToId($scope, resName) {
  for (var i in $scope.marketPrices) {
    if ($scope.marketPrices[i].name == resName) return $scope.marketPrices[i].id
  }
}

function unitToFactor(unit) {
  switch(unit) {
    case "NO UNITS":
      return 1;
      break;
    case "K":
      return 1000;
      break;
    case "M":
      return 1000000;
      break;
    case "B":
      return 1000000000;
      break;
    case "T":
      return 1000000000000;
      break;
  }
  return NaN;
}

function getResourceId(resName) {
  $scope.resources
}

function updateMarket($scope, marketPrices) {
  $scope.marketPrices = marketPrices;
  $scope.marketPrices.sort(function (u1, u2) {
    return u1.id - u2.id;
  })
  $scope.marketPrices.splice(3,1)
}

function marketBuyIsValid(resourceId, amount, unitFactor) {
  var isFormCorrect = true;
  if (!Number.isInteger(parseInt(resourceId))) {
    $("#marketBuyResource").addClass("formError");
    isFormCorrect = false;
  } else {
    $("#marketBuyResource").removeClass("formError")
  }
  if (!(isInteger(amount))) {
    $("#marketBuyAmount").addClass("formError");
    isFormCorrect = false;
  } else {
    $("#marketBuyAmount").removeClass("formError");
  }
  if (isNaN(unitFactor)) {
    $("#marketBuyUnit").addClass("formError");
    isFormCorrect = false;
  } else {
    $("#marketBuyUnit").removeClass("formError");
  }
  return isFormCorrect;
}

function marketSellIsValid(resourceId, amount, unitFactor) {
  var isFormCorrect = true;
  if (!Number.isInteger(parseInt(resourceId))) {
    $("#marketSellResource").addClass("formError");
    isFormCorrect = false;
  } else {
    $("#marketSellResource").removeClass("formError")
  }
  if (!(isInteger(amount))) {
    $("#marketSellAmount").addClass("formError");
    isFormCorrect = false;
  } else {
    $("#marketSellAmount").removeClass("formError");
  }
  if (isNaN(unitFactor)) {
    $("#marketSellUnit").addClass("formError");
    isFormCorrect = false;
  } else {
    $("#marketSellUnit").removeClass("formError");
  }
  return isFormCorrect;
}

function updateUser($scope, user) {
  $scope.user.username = user.username;
  $scope.user.score = user.score;
  $scope.user.image = user.profile_image_url;
}

function updateRank($scope, rank) {
  $scope.user.ranking = rank;
}

function updateWealth($scope, resources) {

  resources.sort(function (res1, res2) {
    return  res2.id - res1.id;
  });

  resources.forEach(function(res){
    $scope.resources[res.id] = {
      name: res.name,
      id: res.id,
      storage: res.storage,
      production: res.production
    }

  });
}

function initializeFactories($scope, factories){

  factories.forEach(function (f) {
    var factInfo = {
      id: f.id,
      type: f.type,
      amount: f.amount,
      level: f.level,
      cost: {},
      recipe: {
        input: {},
        output: {}
      },
      buyLimits: {},
      upgrade: {}
    };

    $scope.factories.push(factInfo);


  });

  $scope.factories.sort(function (f1, f2) {
    return f1.id - f2.id;
  })
}

function updateFactory($scope, factory) {
  $scope.factories[factory.id].type=factory.type;
  $scope.factories[factory.id].amount=factory.amount;
  $scope.factories[factory.id].level=factory.level;
}

function updateFactoryRecipe($scope, factoryID, recipe) {
  var factory = $scope.factories[factoryID];
  factory.cost = {};
  factory.input = {};
  factory.output = {};
  factory.recipe.cantInputs = 0;
  factory.recipe.cantOuputs = 0;

  recipe.forEach(function (elem) {
    if (elem.storage != null) {
      factory.cost[elem.id] = elem;
    }
    if (elem.production < 0) {
      factory.recipe.input[elem.id] = elem;
      factory.recipe.cantInputs += 1;
    }
    if (elem.production > 0) {
      factory.recipe.output[elem.id] = elem;
      factory.recipe.cantOutputs += 1;
    }
  });
}

function updateFactoryUpgrade($scope, factoryID, upgrade) {
  $scope.factories[factoryID].upgrade = {
    type: upgrade.type,
    description: upgrade.description,
    cost: upgrade.cost
  }
  switch (upgrade.type){
    case 0:
      $scope.factories[factoryID].message = $scope.game_inputRed;
      break;
    case 1:
      $scope.factories[factoryID].message = $scope.game_priceRed;
      break;
    case 2:
      $scope.factories[factoryID].message = $scope.game_outputInc;
      break;
  }
}

function updateFactoryBuyLimits($scope, factoryID, buyLimits) {
  $scope.factories[factoryID].buyLimits = buyLimits;
}

function addFactories($scope, factoryID, amount) {
  $scope.factories[factoryID].amount += amount;
}


define(['clickerQuest','services/UserService','services/MarketService', 'services/factoryService', 'services/AccountService'], function(clickerQuest) {

  'use strict';
  clickerQuest.controller('GameCtrl', function($scope, $rootScope, $route, $interval, UserService, factoryService, MarketService, AccountService) {
    $scope.showMarket = false;
    $scope.marketUnits = ['NO UNITS', 'K', 'M', 'B', 'T'];
    $scope.unitToFactor = unitToFactor;
    if(!$rootScope.user) {
      AccountService.getUser().then(function (response) {
        $rootScope.user = response.data;
        $route.reload()
      })
    } else {

      initializeViewModel($scope);

      $scope.userID = $rootScope.user.id;
      $scope.clanID = $rootScope.user.clan_id;
      $scope.formatNumber = formatNumber;

      $scope.checkUpdateMax = function () {
        $scope.factories.forEach(function (f) {
          var cantBuy = false;
          var nextBuyLimits = f.buyLimits.cost_next_max;

          if (nextBuyLimits) {
            nextBuyLimits.forEach(function (r) {
              if ($scope.resources[r.id].storage < r.storage) {
                cantBuy = true
              }
              if ($scope.resources[r.id].production < r.production) {
                cantBuy = true
              }
            });

            if (!cantBuy) {
              $scope.refreshFactoryBuyLimits(f.id);
            }
          }
        });
      };

      UserService.getUser($scope.userID).then(
        function (response) {
          updateUser($scope, response.data)
        },
        function (response) { /*TODO error*/
          console.log(response)
        }
      );

      UserService.getRank($scope.userID).then(
        function (response) {
          updateRank($scope, response.data.rank)
        }
      );

      UserService.getWealth($scope.userID).then(
        function (response) {
          updateWealth($scope, response.data.resources)
        }
      );

      $scope.factories = [];

      $scope.refreshFactoryBuyLimits = function (factoryID) {
        UserService.getFactoryBuyLimits($scope.userID, factoryID).then(
          function (response) {
            updateFactoryBuyLimits($scope, factoryID, response.data)
          }
        );
      };

      $scope.refreshFactory = function (factoryID) {
        UserService.getWealth($scope.userID).then(
          function (response) {
            updateWealth($scope, response.data.resources)
            $scope.checkUpdateMax();
          }
        );

        UserService.getFactory($scope.userID, factoryID).then(
          function (response) {
            updateFactory($scope, response.data)
          }
        );

        UserService.getFactoryRecipe($scope.userID, factoryID).then(
          function (response) {
            updateFactoryRecipe($scope, factoryID, response.data.recipe)
          }
        );

        UserService.getFactoryUpgrade($scope.userID, factoryID).then(
          function (response) {
            updateFactoryUpgrade($scope, factoryID, response.data)
          }
        );

        $scope.refreshFactoryBuyLimits(factoryID);

      };

      UserService.getFactories($scope.userID).then(
        function (response) {
          initializeFactories($scope, response.data.factories);

          response.data.factories.forEach(function (f) {
            $scope.refreshFactory(f.id);
          })

        }
      );

      $scope.updateResources = function () {
        for (var id in $scope.resources) {
          $scope.resources[id].storage += $scope.resources[id].production;
        }

      };

      var myInterval = $interval(function () {
        $scope.checkUpdateMax();
        $scope.updateResources();
      }, 1000);

      $scope.$on("$destroy", function () {
        $interval.cancel(myInterval);
      });

      // NG-CLICK CALLBACKS

      $scope.updateMarketPrices = function () {
        MarketService.getPrices().then(
          function (response) {
            console.log(response.data)
            updateMarket($scope, response.data.market);
          }
        )
      };

      $scope.buyResource = function () {
        var resourceId = $scope.market.buy.resourceID;
        var amount = $scope.market.buy.amount;
        var unitFactor = unitToFactor($scope.market.buy.unit);

      if (marketBuyIsValid(resourceId, amount, unitFactor)) {
        MarketService.buy(resourceId, amount * unitFactor).then(
          function (response) {
            UserService.getWealth($scope.userID).then(
              function (response) {
                updateWealth($scope, response.data.resources)
              }
            );
            showSnackbarMessage($scope.game_buySuccess);
            $scope.showMarket = false;
          },
          function (response) {
            if (response.status == 409) {
              showSnackbarMessage($scope.game_buyTooMuch);
            } else {
              showSnackbarMessage($scope.game_buyFail);
            }
            $scope.showMarket = false;
          }
        );
      }
    };

    $scope.sellResource = function () {
      var resourceId = $scope.market.sell.resourceID
      var amount = $scope.market.sell.amount;
      var unitFactor = unitToFactor($scope.market.sell.unit);

      if (marketSellIsValid(resourceId, amount, unitFactor)) {
        MarketService.sell(resourceId, amount * unitFactor).then(
          function (response) {
            UserService.getWealth($scope.userID).then(
              function (response) {
                updateWealth($scope, response.data.resources)
              }
            );
            showSnackbarMessage($scope.game_sellSuccess);
            $scope.showMarket = false;
          },
          function (response) {
            if (response.status == 409) {
              showSnackbarMessage($scope.game_sellTooMuch);
            } else {
              showSnackbarMessage($scope.game_sellFail);
            }
            $scope.showMarket = false;
          }
        );
      }
    };

      $scope.buyFactory = function (factId, amount) {
        showSnackbarMessage($scope.game_buying)
        factoryService.buyFactory(factId, amount).then(
          function (response) {
            $scope.refreshFactory(factId);
          }
        )
      };

      $scope.buyMaxFactory = function (factId) {
        showSnackbarMessage($scope.game_buyingFact)
        $scope.buyFactory(factId, $scope.factories[factId].buyLimits.max);
      };

      $scope.upgradeFactory = function (factId) {
        factoryService.upgradeFactory(factId).then(
          function (response) {
            $scope.refreshFactory(factId);
          },
          function (error) {
            console.log(error);
          }
        )
      }
    }


  });

});
