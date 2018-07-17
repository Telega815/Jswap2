var fileCounter = 0;
$(document).ready(function(){
    initializeDropZone();

    if (sessionStorage.getItem('clientId')===null){
        getClientID();
    }else{
        getClientInfo();
    }
});

function getClientID() {
    $.ajax({
        headers: headers,
        url: window.location.protocol + "//" + window.location.host + "/restService/getNewClientId",
        method:"POST",
        success:function(data){
            sessionStorage.setItem('clientId', data);
        },
        error: function(e){
            //TODO delete this after debug tests
            alert(e.responseText);
        }
    })
}

function getClientInfo() {

    var formData = new FormData();
    formData.append("clientId", sessionStorage.getItem("clientId"));
    $.ajax({
        url: window.location.protocol +"//"+window.location.host+"/restService/getClientIdInfo?"+csrfParameter+"="+csrfToken,
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
            if(data.haveFiles){
                showUploadBlock();
                var uploadTable = document.getElementById("uploadTable");
                for(var i = 0; i < data.filenames.length; i++){
                    var file = {
                        name: data.filenames[i],
                        size: data.fileSizes[i]
                    };
                    uploadTable.appendChild(showFile(file, data.fileIds[i]));
                }
            }
        },
        error: function (e) {
            alert(e.responseText);
        }
    })
}

function initializeDropZone() {
    var dropZone = document.getElementById("dropZone");
    dropZone.ondrop = function(event) {
        event.preventDefault();
        dropZone.style.height = "50px";
        dropZone.style.backgroundColor = "transparent";
        var files = event.dataTransfer.files;
        showUploadBlock();
        showAndSendFiles(files);
    };
    dropZone.ondragover = function() {
        dropZone.style.backgroundColor = 'gray';
        return false;
    };

    dropZone.ondragleave = function() {
        dropZone.style.backgroundColor = "transparent";
        return false;
    };
}

function inputAction(event) {
    var files = event.target.files;
    showAndSendFiles(files);
}
//show/hideUploadBlock-------------------------------------------------------
function showUploadBlock() {
    document.getElementById("dropZone").style.display = "none";
    document.getElementById("uploadContainer").style.display = "inline-block";
}

function hideUploadBlock() {
    document.getElementById("uploadTable").innerHTML = "";
    document.getElementById("uploadComment").innerText = "";
    document.getElementById("dropZone").style.display = "flex";
    document.getElementById("uploadContainer").style.display = "none";
}


//show files in upload block--------------------------------------------------
function showAndSendFiles(files) {
    var uploadTable = document.getElementById("uploadTable");
    for (var i = 0; i < files.length; i++){
        while(document.getElementById("fileRow_"+fileCounter) !== null){
            fileCounter++;
        }
        uploadTable.appendChild(showFile(files[i], fileCounter));
        sendFile(files[i]);
    }
}

function showFile(file, fileId) {

    var fileRow = document.createElement("tr");
    fileRow.id = "fileRow_" + fileId;
    fileRow.className = "UploadedFileRow";

    var th1 = document.createElement("th");
    th1.className = "th thOne";
    th1.title = file.name;
    th1.innerText = file.name;
    th1.id = "file_" + fileId;
    sliceTextForUploadContainer(th1);

    var th2 = document.createElement("th");
    th2.innerText = formatSize(file.size);
    th2.className = "th";

    var th4 = document.createElement("th");
    th4.id = "deleteTmp_"+ fileId;
    th4.className = "thDelete";
    th4.addEventListener("click", deleteTmpFile);

    var img = document.createElement("img");
    img.id = "deleteTmp_"+ fileId;
    img.src = "/resources/media/feeds/donloadCancel.png";
    th4.appendChild(img);

    fileRow.appendChild(th1);
    fileRow.appendChild(th2);
    fileRow.appendChild(th4);

    return fileRow;
}

//send file to server--------------------------------------------------------
function sendFile(file) {
    var formData = new FormData();
    formData.append("file", file, file.name);

    while (sessionStorage.getItem('clientId')===null){
        getClientID();
    }
    formData.append("clientId", sessionStorage.getItem('clientId'));
    formData.append("fileId", fileCounter);

    $.ajax({
        url: window.location.protocol + "//" + window.location.host + "/restService/uploadFile?"+csrfParameter+"="+csrfToken,
        enctype: "multipart/form-data",
        method: 'POST',
        type: 'POST',
        data: formData,
        cache: false,
        contentType: false,
        processData: false,
        xhr: function () {
            var req = $.ajaxSettings.xhr();
            var reqUp = req.upload;
            reqUp.id = "xhr_"+fileCounter;
            reqUp.addEventListener('progress', uploadProgress, false);
            return req;
        },
        success: function (data) {
            //TODO some shit here i donno
        },
        error: function (e) {
            alert(e.responseText);
        }
    });
}
//----------------------------------------------------------------------------
function uploadProgress(event) {
    var progressStatus = document.getElementById("fileRow_"+event.target.id.split("_")[1]);
    var percent = parseInt(event.loaded / event.total * 100);
    progressStatus.style.background = "linear-gradient(90deg, #18ff00 " + percent + "%, #FFF " + percent +"%)";
}

//----------------------------------------------------------------------------
function deleteTmpFile(event) {
    var id = event.target.id.split('_')[1];
    var formData = new FormData();
    formData.append("clientId", sessionStorage.getItem("clientId"));
    formData.append("fileId", id);
    $.ajax({
        url: window.location.protocol +"//"+window.location.host+"/restService/deleteFile?"+csrfParameter+"="+csrfToken,
        enctype: "multipart/form-data",
        method: "POST",
        data: formData,
        cache: false,
        contentType: false,
        processData: false,
        success: function (data) {
            if (data === true){
                var row = document.getElementById("fileRow_"+ id);
                row.parentNode.removeChild(row);
            }else{
                alert(data);
            }
        },
        error: function (e) {
            alert(e.responseText);
        }
    })
}

//----------------------------------------------------------------------------
function saveFiles() {
    var comment = document.getElementById('uploadComment');
    var feeds = document.getElementsByClassName("input-hidden");
    var currentFeedId;
    for (var i = 0; i < feeds.length; i++){
        if(feeds[i].getAttribute("checked") === "checked"){
            currentFeedId = feeds[i];
            break;
        }
    }
    var filesToSave = document.getElementsByClassName("UploadedFileRow");
    var obj = {
        postComment: comment.innerText,
        feedId: currentFeedId,
        filesToSave: filesToSave,
        clientId: sessionStorage.getItem("clientId")
    };
    var data = JSON.stringify(obj);
    $.ajax({
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        url: window.location.protocol +"//"+window.location.host+"/restService/saveNewPost?"+csrfParameter+"="+csrfToken,
        method: 'POST',
        data: data,
        success: function(data){

            // var div = document.createElement('div');
            // div.innerHTML = data.htmlPost;
            //
            // var mainCenter = document.getElementById("mainCenter");
            // mainCenter.insertBefore(div.firstChild, mainCenter.childNodes[1]);
            // hidePostEdit(data.postId);
            //
            // hideUploadBlock();
        },
        error: function (e) {
            alert(e.responseText);
        }
    });
}
