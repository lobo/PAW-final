
var toPrint = window.sessionStorage.getItem("asdasd");

if(toPrint !== null){
    sessionStorage.removeItem("asdasd");
    Materialize.toast(toPrint,4000);
}

if (document.getElementById("clan-action") != null) {
    document.getElementById("clan-action").addEventListener("click", function () {
        switch (document.getElementById("clan-action").dataset.action) {
            case "join":
                $.post(contextPath + "/joinClan",
                    {
                        clanName: clanName
                    }, function(data) {
                        window.location = contextPath + "/clan/" + clanName;
                    });
                break;
            case "leave":
                $.post(contextPath + "/leaveClan",
                    {
                        clanName: clanName
                    }, function(data) {
                        window.location = contextPath + "/game";
                    });
                break;
        }
    });
}

var users = document.getElementsByClassName("username-link");
for(var i = 0; i<users.length; i++) {
    var user = users.item(i)
    user.addEventListener("click", function () {
        console.log(contextPath + "/u/" + user.dataset.username);
        window.location = contextPath + "/u/" + user.dataset.username;
    })
}

var clans = document.getElementsByClassName("clanname-link");
for(var i = 0; i<clans.length; i++) {
    var clan = clans.item(i)
    clan.addEventListener("click", function () {
        console.log(contextPath + "/clan/" + this.dataset.clanname);
        window.location = contextPath + "/clan/" + this.dataset.clanname
    })
}

window.onkeyup = function(e) {
    var key = e.keyCode ? e.keyCode : e.which;

    if (key == 13) {
        if($( "#search" ).is(":focus")){
            searchCommunity(true);
        }
    }
};


