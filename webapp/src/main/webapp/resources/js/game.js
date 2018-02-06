var url=window.location.href.split("/");
var userId =url[url.length - 2];
sessionStorage.removeItem("user");

var buyabilityJSON = [];
var limitantResources = [];

$(document).ready(function(){
    $('.modal').modal();
    $('select').material_select();
});

window.onkeyup = function(e) {
    var key = e.keyCode ? e.keyCode : e.which;

    if (key == 13) {
        if($( "#clanNameInput" ).is(":focus")){
            createClanFunction();
        } else if($( "#search" ).is(":focus")){
            searchCommunity(true);
        }
    }
};

var toPrint = window.sessionStorage.getItem("message");

if(toPrint !== null){
    sessionStorage.removeItem("message");
    Materialize.toast(toPrint,4000);
}

refreshValues(false);
refreshView();
buyabilityPOST();
refreshUpgradesBuyability();

function refreshValues(update){
    Object.keys(storagesMap).forEach(function (key,value) {
        storagesMap[key] = storagesMap[key] + productionsMap[key]
    });
}

function dec(mesageCode, arg0, arg1, arg2) {
    var st = messages[mesageCode];
    if(st == null) {
        console.log(mesageCode + ":no message");
        return;
    }
    st = st.replace("{0}",arg0);
    st = st.replace("{1}",arg1);
    st = st.replace("{2}",arg2);
    return st;
}
function decRes(resourceId) {
    return messages.resources[resourceId];
}
function decFac(factoryId) {
    return messages.factories[factoryId];
}

function refreshView() {
    unitPerSec= "/s"

    storageValues = $(".resourcesValue");
    for (var i = 0; i<storageValues.size() ; i++) {
        element = storageValues.eq(i);
        res = element.data("resource");
        element.text(String(abbreviateNumber(storagesMap[res], false)) + " + " + String(abbreviateNumber(productionsMap[res], true)) + unitPerSec)
    }


    // TODO: make it different... but TIME IS NOT ENOUGH TO MAKE IT WELL
    // update sell price
    var unit = document.getElementById("market.sell.unit").value
    var multiplier
    switch (unit){
        case "K": multiplier = 1000; break;
        case "M": multiplier = 1000000; break;
        case "B": multiplier = 1000000000; break;
        case "T": multiplier = 1000000000000; break;
        case "none":
        default: multiplier = 1; break
    }
    var resourceId = document.getElementById("market.sell.resources").value;
    var quantity = document.getElementById("market.sell.quantity").value;
    if(resourceId != "" && quantity != "" && unit != "") {
        document.getElementById("market.sell.price").innerHTML = dec("game.market.profit") +abbreviateNumber(parseInt(quantity) * multiplier * costBuyResources[resourceId])
    } else {
        document.getElementById("market.sell.price").innerHTML = ""
    }

    // update buy price
    var unit = document.getElementById("market.buy.unit").value
    var multiplier
    switch (unit){
        case "K": multiplier = 1000; break;
        case "M": multiplier = 1000000; break;
        case "B": multiplier = 1000000000; break;
        case "T": multiplier = 1000000000000; break;
        case "none":
        default: multiplier = 1; break
    }
    resourceId = document.getElementById("market.buy.resources").value;
    quantity = document.getElementById("market.buy.quantity").value;
    if(resourceId != "" && quantity != "" && unit != "") {
        document.getElementById("market.buy.price").innerHTML = dec("game.market.cost")  + abbreviateNumber(parseInt(quantity) * multiplier * costBuyResources[resourceId])
    } else {
        document.getElementById("market.buy.price").innerHTML = ""
    }
}

function refreshUpgradesBuyability() {
    var notBuyable= []
    var availableMoney = storagesMap[3] // 3: money
    for (factId in upgradesCost) {
        if (upgradesCost[factId] > availableMoney) {
            document.getElementById("upgradeDisabler" + factId).classList.remove("canBuy");
        } else {
            document.getElementById("upgradeDisabler" + factId).classList.remove("canBuy");
            document.getElementById("upgradeDisabler" + factId).classList.add("canBuy");
        }
    }
}

function getRemainingResourcesTooltipMsg(storagesLimitant, productionsLimitant) {
    var storageTxt = messages["game.storageRemaining"] + '\n';
    var cant = 0;
    for (var resourceLimitant in storagesLimitant) {
        cant++;
        storageTxt += "  " + messages[resIdToName[resourceLimitant].toLowerCase()] + ": " + abbreviateNumber(storagesLimitant[resourceLimitant]) + "\n";
    }
    if (cant == 0) storageTxt = "";

    var productionTxt = messages["game.productionRemaining"] + '\n';
    cant = 0;
    for (var resourceLimitant in productionsLimitant) {
        cant++;
        productionTxt += "  " + messages[resIdToName[resourceLimitant].toLowerCase()] + ": " + abbreviateNumber(productionsLimitant[resourceLimitant]) + "/s\n";
    }
    if (cant == 0) productionTxt = "";

    return storageTxt + productionTxt;
}

function refreshFactoriesBuyability() {

    for (var factId = 0; factId < factInOrder.length; factId++) {
        var factBuyability = buyabilityJSON[factId]
        var maxBuy = factBuyability.maxBuy

        if (maxBuy <= 0) {
            document.getElementById("factoryMaxDisabler" + factIdToName[factId]).classList.remove("canBuy")
            document.getElementById("maxBuy" + factId).innerHTML = messages["game.max"] + " " + 0;
        } else {
            document.getElementById("factoryMaxDisabler" + factIdToName[factId]).classList.add("canBuy")
            document.getElementById("maxBuy" + factId).innerHTML = messages["game.max"] + " " + abbreviateNumber(maxBuy);
        }
        if (maxBuy >= 1) {
            document.getElementById("factory1Disabler" + factIdToName[factId]).classList.add("canBuy");
        } else {
            document.getElementById("factory1Disabler" + factIdToName[factId]).classList.remove("canBuy");
            var productionsLimitant = factBuyability[1].prod;
            var storagesLimitant = factBuyability[1].storage;

            var tooltipTxt = getRemainingResourcesTooltipMsg(storagesLimitant, productionsLimitant);
            replaceTooltip("factory1Disabler" +  factIdToName[factId],tooltipTxt)
        }
        if (maxBuy >= 10) {
            document.getElementById("factory10Disabler" + factIdToName[factId]).classList.add("canBuy");
        } else {
            document.getElementById("factory10Disabler" + factIdToName[factId]).classList.remove("canBuy");
            var productionsLimitant = factBuyability[10].prod;
            var storagesLimitant = factBuyability[10].storage;

            var tooltipTxt = getRemainingResourcesTooltipMsg(storagesLimitant, productionsLimitant);
            replaceTooltip("factory10Disabler" +  factIdToName[factId],tooltipTxt)
        }
        if (maxBuy >= 100) {
            document.getElementById("factory100Disabler" + factIdToName[factId]).classList.add("canBuy");
        } else {
            document.getElementById("factory100Disabler" + factIdToName[factId]).classList.remove("canBuy");
            var productionsLimitant = factBuyability[100].prod;
            var storagesLimitant = factBuyability[100].storage;

            var tooltipTxt = getRemainingResourcesTooltipMsg(storagesLimitant, productionsLimitant);
            replaceTooltip("factory100Disabler" +  factIdToName[factId],tooltipTxt)
        }

    }

    function replaceTooltip(id,tooltipTxt) {
        var element = $("#" + id);
        var lastTooltip = element.attr("data-tooltip")
        if(tooltipTxt != lastTooltip) {
            element.attr("data-tooltip", tooltipTxt)
            element.tooltip();
        }
    }
}
setInterval(function(){
    refreshValues(true);
    refreshView();
    buyabilityPOST();
    refreshUpgradesBuyability();
}, 1000);

function buyabilityPOST() {
    $.post(contextPath + "/canBuyFactory",
        {}, function (data) {
            var resp = JSON.parse(data);

            buyabilityJSON = resp.buyables;
            refreshFactoriesBuyability();
        });
}

// Create clan listener
var createClanFunction = function () {
    var clanName = document.getElementById("clanNameInput").value;
    var ret;
    if (clanName.length > 25 || !clanName.match("^[a-zA-Z\+\-\.\_\*]+$")) {
        ret = true;
        document.getElementById("clanNameInput").classList.add("inputerror")
    } else {document.getElementById("clanNameInput").classList.remove("inputerror")}
    if (ret) {return;}

    $.post(contextPath + "/createClan",
        {
            clanName: clanName
        }, function(data) {
            var resp = JSON.parse(data);

            var msg;
            if (resp.result == "exists") {
                msg = dec("game.msg.clanAlreadyExists");
                window.sessionStorage.setItem("message", msg);
                location.reload();
            } else if(resp.result == "noUser") {
            } else {
                window.sessionStorage.setItem("asdasd",messages["clan.created"]);
                window.location = contextPath + "/clan/" + clanName;
            }
        });
}
document.getElementById("createClanSend").addEventListener("click", createClanFunction);

// Buy listener
$.each($(".buyFactory"),function (i,element){
    $("#" + element.id).clickSpark({
        particleImagePath: contextPath + '/resources/star_icon.png',
        particleCount: 55,
        particleSpeed: 10,
        particleSize: 12,
        particleRotationSpeed: 20,
        animationType:'explosion',
        callback: function() {
            document.getElementById("loading").classList.remove("hidden");
            document.getElementById("loading-disabler").classList.remove("hidden");

            var amount = $("#"+element.id).data("amount");
            var factoryId = $("#"+element.id).data("factoryid")
            if (amount == "max") amount = buyabilityJSON[factoryId]["maxBuy"];
            buyFactory(factoryId,amount);
        }
    })
});

// $.each($(".maxText"), function (i, element) {
//     document.getElementById(element.id).addEventListener("click", function () {
//         document.getElementById($("#"+element.id).data("refersto")).click();
//     })
// });

function buyEffect() {

}


// Upgrade listener
$.each($(".upgradeButton"),function (i,element){
    $("#" + element.id).clickSpark({
        particleImagePath: contextPath + '/resources/star_icon.png',
        particleCount: 55,
        particleSpeed: 10,
        particleSize: 12,
        particleRotationSpeed: 20,
        animationType:'explosion',
        callback: function() {
            document.getElementById("loading").classList.remove("hidden");
            document.getElementById("loading-disabler").classList.remove("hidden");
            upgradeFactory($("#"+element.id).data("factoryid"))
        }
    })
});

function buyFactory(id,amount){
    $.post(contextPath + "/buyFactory",
        {
            factoryId: id,
            amount: amount,
        }, function(data) {
            var resp = JSON.parse(data);
            var msg ;
            if(resp.result){
                msg = dec("game.factoryBuySuccessful",dec(decFac(resp.factoryId)));
            } else {
                msg = dec("game.factoryBuyFail",dec(decFac(resp.factoryId)));
            }

            window.sessionStorage.setItem("message",msg);
            location.reload();
        });
}

function upgradeFactory(factId) {
    $.post(contextPath + "/upgradeFactory",
        {
            factoryId: factId //$("#"+element.id).data("factoryid")
        }, function(data) {
            var resp = JSON.parse(data);

            var msg ;
            if(resp.result){
                msg = dec("game.upgradeSuccessful",resp.level,dec(decFac(resp.factoryId)));
            } else {
                msg = dec("game.upgradeFail",resp.level,dec(decFac(resp.factoryId)));
            }
            window.sessionStorage.setItem("message",msg);
            location.reload();
    });
}

document.getElementById("market.buy").addEventListener("click", function() {
        var unit = document.getElementById("market.buy.unit").value
        var multiplier
        switch (unit){
            case "K": multiplier = 1000; break;
            case "M": multiplier = 1000000; break;
            case "B": multiplier = 1000000000; break;
            case "T": multiplier = 1000000000000; break;
            case "P": multiplier = 1000000000000000; break;
            case "none":
            default: multiplier = 1; break
        }
        var resourceId = document.getElementById("market.buy.resources").value;
        var quantity = document.getElementById("market.buy.quantity").value;

        var ret = false;
        if (resourceId == "") {
            ret = true;
            document.getElementById("market-buy-resource-wrapper").classList.add("error")
        } else {document.getElementById("market-buy-resource-wrapper").classList.remove("error")}
        if (!quantity.match("^[0-9]+$")) {
            ret = true;
            document.getElementById("market.buy.quantity").classList.add("inputerror")
        } else {document.getElementById("market.buy.quantity").classList.remove("inputerror")}
        if (unit == "") {
            ret = true;
            document.getElementById("market-buy-unit-wrapper").classList.add("error")
        } else {document.getElementById("market-buy-unit-wrapper").classList.remove("error")}
        if (ret) {return;}

        var quantity = parseFloat(quantity)*multiplier;

        if (validateBuy(resourceId, quantity)) {
            $.post(contextPath + "/buyFromMarket",
                {
                    resourceId: resourceId,
                    quantity: quantity
                }, function (data) {
                    var resp = JSON.parse(data);

                    var msg ;
                    if(resp.result){
                        msg = dec("game.market.buySuccessful",abbreviateNumber(resp.quantity,false),dec(decRes(resp.resourceId)));
                    } else {
                        msg = dec("game.market.buyFail",abbreviateNumber(resp.quantity,false),dec(decRes(resp.resourceId)));
                    }
                    window.sessionStorage.setItem("message",msg);
                    location.reload();
            });
        }
    });


function validateBuy(resourceId, quantity) {
    if (storagesMap[3] >= quantity * costBuyResources[resourceId]) { //3: MONEY
        return true;
    }
    Materialize.toast(dec("game.market.buyFail",abbreviateNumber(quantity,false),dec(decRes(resourceId))), 3000);

    return false;
}

document.getElementById("market.sell").addEventListener("click", function() {
            var unit = document.getElementById("market.sell.unit").value
            var multiplier
            switch (unit){
                case "K": multiplier = 1000; break;
                case "M": multiplier = 1000000; break;
                case "B": multiplier = 1000000000; break;
                case "T": multiplier = 1000000000000; break;
                case "P": multiplier = 1000000000000000; break;
                case "none":
                default: multiplier = 1; break
            }
            var resourceId = document.getElementById("market.sell.resources").value;
            var quantity = document.getElementById("market.sell.quantity").value;

            var ret = false;
            if (resourceId == "") {
                ret = true;
                document.getElementById("market-sell-resources-wrapper").classList.add("error")
            } else {document.getElementById("market-sell-resources-wrapper").classList.remove("error")}
            if (!quantity.match("^[0-9]+$")) {
                ret = true;
                document.getElementById("market.sell.quantity").classList.add("inputerror")
            } else {document.getElementById("market.sell.quantity").classList.remove("inputerror")}
            if (unit == "") {
                ret = true;
                document.getElementById("market-sell-unit-wrapper").classList.add("error")
            } else {document.getElementById("market-sell-unit-wrapper").classList.remove("error")}

            if (ret) {return;}

            var quantity = parseFloat(quantity)*multiplier;

            if (validateSell(parseInt(resourceId), quantity)) {
                $.post(contextPath + "/sellToMarket",
                    {
                        resourceId: resourceId,
                        quantity: quantity
                    }, function (data) {
                        var resp = JSON.parse(data);

                        var msg ;
                        if(resp.result){
                            msg = dec("game.market.sellSuccessful",abbreviateNumber(resp.quantity,false),dec(decRes(resp.resourceId)));
                        } else {
                            msg = dec("game.market.sellFail",abbreviateNumber(resp.quantity,false),dec(decRes(resp.resourceId)));
                        }
                        window.sessionStorage.setItem("message",msg);
                        location.reload();
                    });
            }
        });


    function validateSell(resourceId, quantity) {
        if (storagesMap[resourceId] >= quantity) {
            return true;
        }
        Materialize.toast(dec("game.market.sellFail",abbreviateNumber(quantity,false),dec(decRes(resourceId))), 3000);
        return false
    }