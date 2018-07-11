var feedURL;
var uploadButton;
var pinField;

$(document).ready(function(){
    PinInputHide();
    uploadButton = document.getElementById("uploadButton");
    uploadButton.disabled = true;
    document.getElementsByClassName("")
    pinField = document.getElementById("pinInputField");
    $("#enterPinButton").hide();
    checkWriteAccess();
    var href = document.getElementById("viewFilesHref");
    href.addEventListener("click",function(e){e.preventDefault(); checkReadAccess(e)}, false);

});
function PinInputShow(){
    $("#pinBlock").show();
    pinField.focus();

}
function PinInputHide(){
    $("#pinBlock").hide();
}

function checkWriteAccess() {
    $.ajax({
        url: document.URL + "/checkWriteAccess",
        type: "GET",
        success: function (data) {
            if(data === "pin-required"){
                $("#enterPinButton").show();
            }else if(data === "access-granted"){
                uploadButton.disabled = false;
            }else if(data === "authentication-required"){
                alert(data);
            }
        },
        error: function (e) {
            alert(e.responseText);
        }
    });
}

function checkReadAccess( e ) {
    feedURL = e.target.href;
    $.ajax({
        url: document.URL + "/checkReadAccess",
        type: "GET",
        success: function (data) {
            if(data === "pin-required"){
                PinInputShow();
            }else if(data === "access-granted"){
                window.location.replace(feedURL);
            }else if(data === "authentication-required"){
                alert(data);
            }


        },
        error: function (e) {
            alert(e.responseText);
        }
    });
}

function countPinInput(event) {
    if(event.target.value.length === 4){
        checkPin();
    }
}

function checkPin(){
    var pin = pinField.value;
    var checkPinUrl = document.URL + "/service/checkPin";
    $.ajax({
        url: checkPinUrl,
        type: "POST",
        data: pin,
        success: function (data) {
            if (data === "access-granted"){
                PinInputHide();
                if(feedURL === undefined){
                    uploadButton.disabled = false;
                    $("#enterPinButton").hide();
                }else {
                    window.location.replace(feedURL);
                }
            }else{
                alert("DENIED!")
            }
        },
        error: function (e) {
            alert(e.responseText);
        }

    })
}