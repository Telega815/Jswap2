

$(document).ready(function(){
    initializeDropZone();

    if (sessionStorage.getItem('clientId')===null){
        getClientID();
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

function initializeDropZone() {
    var dropZone = document.getElementById("dropZone");
    dropZone.ondrop = function(event) {
        event.preventDefault();
        dropZone.style.height = "50px";
        dropZone.style.backgroundColor = "transparent";
        var files = event.dataTransfer.files;
        showUploadBlock();
        showFiles(files);
        sendFiles(files);
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
    showUploadBlock();
    showFiles(files);
    sendFiles(files);
}

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

function showFiles(files) {
    var uploadTable = document.getElementById("uploadTable");

    for (var i = 0; i < files.length; i++){
        uploadTable.appendChild(showFile(files[i]));
    }
}

function showFile(file) {
    var fileRow = document.createElement("tr");
    fileRow.id = "fileRow_" + fileCount;

    var th1 = document.createElement("th");
    th1.className = "th thOne";
    th1.title = file.name;
    th1.innerText = file.name;
    th1.id = "file_" + fileCount;
    sliceTextForUploadContainer(th1);

    var th2 = document.createElement("th");
    th2.innerText = formatSize(file.size);
    th2.className = "th";

    var th4 = document.createElement("th");
    th4.id = "deleteTmp_"+ fileCount;
    th4.className = "thDelete";
    th4.addEventListener("click", deleteTmpFile);

    var img = document.createElement("img");
    img.id = "deleteTmp_"+ fileCount;
    img.src = "/resources/media/feeds/donloadCancel.png";
    th4.appendChild(img);

    fileRow.appendChild(th1);
    fileRow.appendChild(th2);
    fileRow.appendChild(th4);

    fileCount++;
    return fileRow;
}

function sendFiles(files) {
    for (var i = 0; i < files.length; i++) {
        var formData = new FormData();
        formData.append("file", files[i], files[i].name);

        while (sessionStorage.getItem('clientId')===null){
            getClientID();
        }
        formData.append("id", sessionStorage.getItem('clientId'));
        formData.append()
dsdfsdfsdfsdfsdfsdsdfs
        $.ajax({
            url: window.location.protocol + "//" + window.location.host + "/restService/uploadFile?${_csrf.parameterName}=${_csrf.token}",
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
                reqUp.id = j.toString();
                reqUp.addEventListener('progress', uploadProgress, false);
                j++;
                return req;
            },
            success: function (data) {

            },
            error: function (e) {
                alert(e.responseText);
            }
        });
    }
}

function uploadProgress(event) {
    var progressStatus = document.getElementById("fileRow_"+event.target.id);
    var percent = parseInt(event.loaded / event.total * 100);
    progressStatus.style.background = "linear-gradient(90deg, #18ff00 " + percent + "%, #FFF " + percent +"%)";
}

function deleteTmpFile(event) {
    var id = event.target.id.split('_')[1];
    var filename = document.getElementById("file_"+id).title;
    var url = document.URL + "/deleteTmpFile/" + filename;
    $.ajax({
        url: url,
        method: "GET",
        success: function (data) {
            alert(data);
            document.getElementById("fileRow_"+ id).style.display = "none";
        },
        error: function (e) {
            alert(e.responseText);
        }
    })
}

function saveFiles() {
    var comment = document.getElementById('uploadComment');
    var feedname = document.getElementById('selectFeed').value;
    var obj = {
        comment: comment.innerText,
        feedName: feedname,
        postID: -1,
        filesToDelete: filesToDelete
    };
    var data = JSON.stringify(obj);
    $.ajax({
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        url: document.URL + "/save",
        method: 'POST',
        data: data,
        success: function(data){
            var div = document.createElement('div');
            div.innerHTML = data.htmlPost;

            var mainCenter = document.getElementById("mainCenter");
            mainCenter.insertBefore(div.firstChild, mainCenter.childNodes[1]);
            hidePostEdit(data.postId);

            hideUploadBlock();
        },
        error: function (e) {
            alert(e.responseText);
        }
    });
}