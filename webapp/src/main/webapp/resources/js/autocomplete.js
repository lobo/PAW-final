
var clanImageSrc = "http://simpleicon.com/wp-content/uploads/multy-user.png";
var userImageSrc = "https://www.bosserpropiedades.com/wp-content/uploads/2016/03/foto-perfil-2.png";
var autocomplete_id;


$(document).ready(function(){
    startAutocomplete($("#search"))
});

function startAutocomplete(id) {
    autocomplete_id = id;
    autocomplete_id.autocomplete({
        limit: 5, // The max amount of results that can be shown at once. Default: Infinity.
        minLength: 1 // The minimum length of the input for the autocomplete to start. Default: 1.
    });
    autocomplete_id.on('change textInput input', function () {
        var searchTerm = this.value;
        if(searchTerm.length > 1){
            searchCommunity();
        }
    });
}

var searchCommunity = function (direct) {
// Create clan listener
    var search = autocomplete_id.val();
    console.log("Searching clan: " + search);
    $.post(contextPath + "/search",
        {
            search: search
        }, function(data) {
            var resp = JSON.parse(data);
            var autoData = {};
            $('#search').autocomplete({
                data: {}});
            var temp;
            for(var i=0;i<resp.users.length;i++){
                autoData[resp.users[i]+' ']=userImageSrc;
            }
            for(var i=0;i<resp.clans.length;i++){
                autoData[resp.clans[i]+'  ']=clanImageSrc;
            }
            if(direct){
                if(resp.users.indexOf(search) != -1){
                    window.location.replace(contextPath + "/u/" + search);
                } else if(resp.clans.indexOf(search) != -1){
                    window.location.replace(contextPath + "/clan/" + search);
                }
            }
            $('#search').autocomplete({
                data: autoData,
                onAutocomplete: function(val) {
                    // Callback function when value is autcompleted.
                    if(this.firstChild.src === userImageSrc){
                        window.location = contextPath + "/u/" + val.trim();
                    } else {
                        window.location = contextPath + "/clan/" + val.trim();
                    }
                }
            });
        });
}

function changeSearchLocation(val){

}