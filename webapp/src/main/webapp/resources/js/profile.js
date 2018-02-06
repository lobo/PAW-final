

$('.modal').modal();

var clan = document.getElementById("clanLink");
if(clan != null) {
    clan.addEventListener("click", function () {
        console.log("redirigiendo...!")
        window.location = contextPath + "/clan/" + clan.dataset.clanname
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
