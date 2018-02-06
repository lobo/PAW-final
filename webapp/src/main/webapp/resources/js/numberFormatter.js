/**
 * Created by epord on 18/06/17.
 */

function abbreviateNumber(value,decimals) {
    var newValue = value;
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