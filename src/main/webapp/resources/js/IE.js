document.addEventListener('DOMContentLoaded', function(){
	if(getInternetExplorerVersion()!==-1){

	  var US = document.getElementsByClassName('userContainer');
	  US[0].style.position = "fixed",
	  US[0].style.top = "0";
	  document.getElementsByClassName('mainCenter')[0].style.marginLeft = "360px";
	  document.getElementsByClassName('mainRight')[0].style.position = "fixed";
      document.getElementsByClassName('comment')[0].style.maxHeight = "28vh";
      document.getElementsByClassName('uploadStatus')[0].style.maxHeight = "28vh";
	}
	else {
	}
})

function getInternetExplorerVersion()
{
    var rv = -1;
    if (navigator.appName == 'Microsoft Internet Explorer')
    {
        var ua = navigator.userAgent;
        var re  = new RegExp("MSIE ([0-9]{1,}[\.0-9]{0,})");
        if (re.exec(ua) != null)
            rv = parseFloat( RegExp.$1 );
    }
    else if (navigator.appName == 'Netscape')
    {
        var ua = navigator.userAgent;
        var re  = new RegExp("Trident/.*rv:([0-9]{1,}[\.0-9]{0,})");
        if (re.exec(ua) != null)
            rv = parseFloat( RegExp.$1 );
    }
    return rv;
}


