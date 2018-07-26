window.addEventListener("DOMContentLoaded", init);

function init() {
	var advancedOptions=document.getElementById('advancedOptions');
	var advancedOptionsShow=document.getElementById('advancedOptionsShow');
    advancedOptionsShow.style.height = "0px";
    advancedOptions.addEventListener('click', openCloseAdvancedOptions);
}

function openCloseAdvancedOptions() {
    var advancedOptions=document.getElementById('advancedOptions');
    var advancedOptionsShow=document.getElementById('advancedOptionsShow');

    if (advancedOptionsShow.style.height === "0px") {
        advancedOptions.classList.add("advancedOptionsClass");
        advancedOptionsShow.style.height = "140px";
    }
    else {
        advancedOptions.classList.remove("advancedOptionsClass");
        advancedOptionsShow.style.height = "0px";
    }
}

function closeAdvancedOptions() {
    var advancedOptions=document.getElementById('advancedOptions');
    var advancedOptionsShow=document.getElementById('advancedOptionsShow');

    if (advancedOptionsShow.style.height !== "0px") {
        advancedOptions.classList.remove("advancedOptionsClass");
        advancedOptionsShow.style.height = "0px";
    }
}

function checkFeedName(event) {
    var name = event.target.value;
    var error = document.getElementById("feedCreatingErrorSpan");
    var english = /^[A-Za-z0-9]*$/;
    var feeds = document.getElementsByClassName("feeds");
    var nameValid = true;
    if (name === "" || !english.test(name)){
        error.innerText = "Invalid characters";
        nameValid = false;
    }else{
        for (var i = 0; i < feeds.length; i++){
            if (feeds[i].innerText.toUpperCase() === name.toUpperCase()){
                error.innerText = "Feed already exists";
                nameValid = false;
                break;
            }
        }
    }
    if (!nameValid){
        error.style.visibility = "visible";
    }else{
        error.style.visibility = "hidden";
    }
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
    document.getElementById("feedName").value="";
    document.getElementById("modeRead").selectedIndex=0;
    document.getElementById("modeWrite").selectedIndex=0;
    document.getElementById("limitSize").value="";
    document.getElementById("sizeType").selectedindex=0;
    closeAdvancedOptions();


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
        headers: {
            'Accept': 'application/json'
        },
        enctype: "multipart/form-data",
        method: "POST",
        data: formData,
        cache: false,
        contentType: false,
        processData: false,
        success: function (data) {
            if (data.status==="Failure"){
                alert(data.html);
            }
            else{
                var tableFeeds = document.getElementById("tableFeeds");
                var div = document.createElement("table");

                div.innerHTML = data.html;
                var tr = div.firstChild;
                tableFeeds.appendChild(tr);
                document.getElementById("feed_" +data.id).onclick = getPostsOfFeed;


                hideCreateFeedWindow();

            }

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