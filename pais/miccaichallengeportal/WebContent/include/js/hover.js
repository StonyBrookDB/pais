function hoverbox_show(obj,src) {
	var div = document.getElementById('hoverbox');
    var oTop = 40,
        oLeft = -250;
    do {
        oLeft += obj.offsetLeft;
        oTop += obj.offsetTop;
    } while (obj = obj.offsetParent);
    div.style.top  = (oTop  + 20) + 'px';
    div.style.left = (oLeft + 20) + 'px';
    div.innerHTML = '<img src="'+src+'" width=600px height=435px/>';
    div.style.visibility = 'visible';
}
function hoverbox_hide() {
	var div=document.getElementById('hoverbox');
    div.style.visibility = 'hidden';
}