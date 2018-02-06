
var prev = document.getElementById("prevPage")
if (prev != null) {
    prev.addEventListener("click", function () {
        window.location = contextPath + "/clanRanking/" + (pageNumber - 1)
    });
}


var next = document.getElementById("nextPage")
if (next != null) {
    next.addEventListener("click", function () {
        window.location = contextPath + "/clanRanking/" + (pageNumber + 1)
    });
}

var users = document.getElementsByClassName("username-link");
for(var i = 0; i<users.length; i++) {
    var user = users.item(i)
    user.addEventListener("click", function () {
        console.log(contextPath + "/clan/" + this.dataset.username);
        window.location = contextPath + "/clan/" + this.dataset.username
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