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
function sendFormCreateFeed() {

    var feedName = document.getElementById("feedName");
    var modeRead = document.getElementById("modeRead");
    var modeWrite = document.getElementById("modeWrite");
    var limitSize = document.getElementById("limitSize");
    var sizeType = document.getElementById("sizeType");
    if (limitSize.value===""){
        limitSize.value="0"
    }
    //feedName='asdasds';
    //modeRead='ASDASD';

    var formData = new FormData();
    formData.append("feedName", feedName.value);
    formData.append("modeRead", modeRead.selectedIndex);
    formData.append("modeWrite", modeWrite.selectedIndex);
    formData.append("limitSize", limitSize.value);
    formData.append("sizeType", sizeType.selectedIndex);

    $.ajax({
        url: window.location.protocol +"//"+window.location.host+"/restService/createFeed?"+csrfParameter+"="+csrfToken,
        enctype: "multipart/form-data",
        method: "POST",
        data: formData,
        cache: false,
        contentType: false,
        processData: false,
        success: function (data) {
            alert(data);

            // if (!data.nullPost){
            //     document.getElementById("mainCenter").removeChild(document.getElementById("FeedContainer_"+selectedPost));
            //
            //     var tdRight = document.getElementsByClassName("tdRight");
            //     //UpdateFeedAndUserSpace
            //     for (var i=0; i<tdRight.length;i++){
            //
            //         if (tdRight[i].id.split("_")[1]==selectedFeed){
            //             tdRight[i].innerText = "Занято " + formatSize(data.feedSize);
            //
            //         }
            //     }
            //     document.getElementById("leftPanelUserFilesSize").innerText = (data.userSpace/1024/1024/1024).toFixed(2);
            //     updateUserSpace();
            // }

        },
        error: function (e) {
            alert(e.responseText);
        }

    })



}