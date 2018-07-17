var imjValid;
var imjInvalid;
var errorFields;
$(document).ready(function(){
    imjValid = document.getElementsByClassName("checkedPhoto");
    imjInvalid = document.getElementsByClassName("checkedPhotoError");
    errorFields = document.getElementsByClassName("errorField");
    document.getElementById("inputPinCod").value = "";
    for(var i =0; i < imjValid.length; i++){
        imjValid[i].hidden = true;
    }
});
function enableSubmit() {
    var res = true;
    for(var i =0; i < imjValid.length; i++){
        if (imjValid[i].hidden === true){
            res = false;
        }
    }
    if (res){
        var s = document.getElementById("submitRegForm");
        s.disabled = false;
    }
}
function disableSubmit() {
    var s = document.getElementById("submitRegForm");
    s.disabled = true;
}

function checkUsername(event) {
    var username = event.target.value;
    if (username === ""){
        imjInvalid[0].hidden = false;
        imjValid[0].hidden = true;
        return;
    }
    $.ajax({
        url: window.location.protocol +"//" + window.location.host + "/service/checkUsername?"+csrfParameter+"="+csrfToken,
        type: "POST",
        data: username,
        success: function (data) {
            if(data === "valid"){
                imjInvalid[0].hidden=true;
                imjValid[0].hidden=false;
                errorFields[0].textContent = "";
                enableSubmit();
            }else{
                imjInvalid[0].hidden=false;
                imjValid[0].hidden=true;
                errorFields[0].textContent = data;
                disableSubmit();
            }
        },
        error: function (e) {
            alert(e.responseText);
        }
    })
}

function checkPwd(event) {
    var pwd = event.target.value;
    comparePwdsPrivate(pwd, document.getElementById("inputFieldPasswordRepeat").value)
    var english = /^[A-Za-z0-9]*$/;
    if (english.test(pwd) && pwd.length>5){
        imjInvalid[1].hidden=true;
        imjValid[1].hidden=false;
        errorFields[1].textContent = "";
        enableSubmit();
    }else {
        imjInvalid[1].hidden=false;
        imjValid[1].hidden=true;
        errorFields[1].textContent = "Pwd must be on eng and have at least 6 symbols"
        disableSubmit();
    }
}
function comparePwds(event) {
    var pwd = document.getElementById("inputFieldPassword").value;
    var pwd2 = event.target.value;
    comparePwdsPrivate(pwd, pwd2);
}

function comparePwdsPrivate(pwd, pwd2) {
    if (pwd2.toLowerCase() === pwd.toLowerCase()){
        imjInvalid[2].hidden=true;
        imjValid[2].hidden=false;
        errorFields[2].textContent = "";
        enableSubmit();
    }else {
        imjInvalid[2].hidden=false;
        imjValid[2].hidden=true;
        errorFields[2].textContent = "passwords doesn't match";
        disableSubmit();
    }
}
function checkEmail(event) {
    var email = event.target.value;
    var emailRegular = /^(([^<>()[\]\\.,;:\s@\"]+(\.[^<>()[\]\\.,;:\s@\"]+)*)|(\".+\"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
    if (emailRegular.test(email.toLowerCase())){
        imjInvalid[3].hidden=true;
        imjValid[3].hidden=false;
        errorFields[3].textContent = "";
        enableSubmit();
    }else {
        imjInvalid[3].hidden=false;
        imjValid[3].hidden=true;
        errorFields[3].textContent = "email is not valid";
        disableSubmit();
    }
}

function validPin(event) {
    var pin = event.target.value;
    var pinRegular = /^[0-9]*$/;
    if (pinRegular.test(pin) && pin.length === 4){
        imjInvalid[4].hidden=true;
        imjValid[4].hidden=false;
        errorFields[4].textContent = "";
        enableSubmit();
    }else {
        imjInvalid[4].hidden=false;
        imjValid[4].hidden=true;
        errorFields[4].textContent = "pin must be 4 digits";
        disableSubmit();
    }
}