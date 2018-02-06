
function isInteger(elem) {
  return String(Math.floor(elem)) === String(elem) && Math.floor(Number(elem)) >= 0;
}

function getParameterByName(name, url) {
  if (!url) url = window.location.href;
  name = name.replace(/[\[\]]/g, "\\$&");
  var regex = new RegExp("[?&]" + name + "(=([^&#]*)|&|#|$)"),
    results = regex.exec(url);
  if (!results) return null;
  if (!results[2]) return '';
  return decodeURIComponent(results[2].replace(/\+/g, " "));
}

function formatDecimal(num, decimals) {
  factor = 10 * decimals;
  return parseFloat(Math.round(num * factor) / factor).toFixed(decimals);
}

/**
 * Created by epord on 18/06/17.
 */

function formatNumber(value,decimals, round) {
  if (round) {
    var newValue = Math.round(value);
  } else {
    var newValue = value;
  }
  var suffixNum =0;
  var suffixes = ["", "K", "M", "B", "T", "P", "X", "Y"];
  if(! decimals || value>=10000) {
    newValue = truncate(value);
  }
  if (newValue >= 10000) {
    suffixNum = Math.floor( (""+newValue).length/3 );
    var shortValue = '';
    for (var precision = 2; precision >= 1; precision--) {
      shortValue = parseFloat( (suffixNum != 0 ? (newValue / Math.pow(1000,suffixNum) ) : newValue).toPrecision(precision));
      var dotLessShortValue = (shortValue + '').replace(/[^a-zA-Z 0-9]+/g,'');
      if (dotLessShortValue.length <= 2) { break; }
    }
    if (shortValue % 1 != 0)  shortNum = shortValue.toFixed(1);
    newValue = shortValue;
  }
  return (decimals ? Math.floor(100* newValue)/100 : newValue)+suffixes[suffixNum];
}

function truncate(number) {
  return number > 0
    ? Math.floor(number)
    : Math.ceil(number);
}


function showSnackbarMessage(message) {
  // Get the snackbar DIV
  var x = document.getElementById("snackbar");
  x.innerHTML =  message;

  // Add the "show" class to DIV
  x.className = "show";

  // After 3 seconds, remove the show class from DIV
setTimeout(function(){ x.className = x.className.replace("show", ""); }, 3000);
}
