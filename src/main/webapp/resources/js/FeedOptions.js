function showOpt(event){
	var options = document.getElementById("ediFeedOptions_"+ event.target.id.split("_")[1]);
	options.style.visibility = "visible";
	options.style.opacity = "1";
}

function hideOpt(event){
	var options = document.getElementById("ediFeedOptions_"+ event.target.id.split("_")[1]);
	options.style.visibility = "hidden";
	options.style.opacity = "0";
}

