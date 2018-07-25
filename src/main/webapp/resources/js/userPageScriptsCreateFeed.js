window.addEventListener("DOMContentLoaded", init);

function init() {
	var advancedOptions=document.getElementById('advancedOptions');
	var advancedOptionsShow=document.getElementById('advancedOptionsShow');
    var advancedOptions=document.getElementById('advancedOptions');
    advancedOptionsShow.style.display='none';

	advancedOptions.addEventListener('click',function() {
		if (advancedOptionsShow.style.display=='none') {
			advancedOptionsShow.style.display='flex';
            advancedOptions.classList.add("advancedOptionsClass");
		}
		  else {
			advancedOptionsShow.style.display='none';
            advancedOptions.classList.remove("advancedOptionsClass");
	  	}
	});
}


function check(e,value) {
    //Check Charater
    var unicode=e.charCode? e.charCode : e.keyCode;
    if (value.indexOf(".") != -1)if( unicode == 46 )
    return false;
    if (unicode!=8)if((unicode<48||unicode>57)&&unicode!=46)
    return false;
}

function checkLength() {
    var fieldLength = document.getElementById('txtF').value.length;
    //Suppose u want 4 number of character
    if (fieldLength <= 4) {
        return true;
    }
    else {
        var str = document.getElementById('txtF').value;
        str = str.substring(0, str.length - 1);
    	document.getElementById('txtF').value = str;
    }
}

function hideCreateFeedWindow() {
    var createFeedWindow = document.getElementById("createFeedWindow");
    createFeedWindow.style.display = "none";
}

function showCreateFeedWindow() {
    var createFeedWindow = document.getElementById("createFeedWindow");
    createFeedWindow.style.display = "flex";
}