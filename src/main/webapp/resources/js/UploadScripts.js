var progressStatuses = [];
var j = 0;
var k = 0;
var fileCount = 0;
var filesToDelete = [];
var enterCounter = 0;

$(document).ready(function(){
    // ----------------------------for IE------------------------
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
    });


//---------------------innerDZ-------------------
    var innerDropZone = document.getElementById("innerDropZone");
    innerDropZone.ondrop = function(event) {
        event.preventDefault();
        innerDropZone.style.backgroundColor = "transparent";
        var files = event.dataTransfer.files;
        showUploadBlock();
        showFiles(files);
        sendFiles(files);
    };
    innerDropZone.ondragover = function() {
        innerDropZone.style.backgroundColor = 'gray';
        return false;
    };

    innerDropZone.ondragleave = function() {
        innerDropZone.style.backgroundColor = "transparent";
        return false;
    };
    var body = document.getElementById("bodyBack");
    body.ondragenter = editPostDragEnter;
    body.ondragleave = editPostDragWindowleave;
});


//---for right upload block







// for edit post----------------------------------------------------------------------
function editPostDrop(event) {
    event.preventDefault();
    var postID = event.target.id.split("_")[1];
    event.target.style.backgroundColor = "transparent";
    var files = event.dataTransfer.files;
    showFilesInPostEdit(postID, files);
    sendFilesFromPostEdit(files);
    editPostDragleave(event);
    editPostDragWindowleave(event);
}

function editPostDragover(event) {
    event.target.style.backgroundColor = 'gray';
    event.target.childNodes[0].style.display = "none";
    event.target.childNodes[1].style.display = "block";
    return false;
}

function editPostDragleave(event) {
    event.target.style.backgroundColor = "transparent";
    event.target.childNodes[1].style.display = "none";
    event.target.childNodes[0].style.display = "block";

    return false;
}
function editPostDragEnter(event) {

    if (editingPost){
        enterCounter++;
        event.preventDefault();
        var hiddenArea = document.getElementById("editPostDragHiddenArea_"+selectedPost);
        var dragArea = document.getElementById("postEditDragArea_"+selectedPost);
        var a = (hiddenArea.scrollHeight - 4);
        dragArea.style.height = a +"px";
        hiddenArea.style.display = "none";
        dragArea.style.display = "flex";
    }else{
        enterCounter++;
        event.preventDefault();
        var dropZone = document.getElementById("dropZone");
        dropZone.style.height = "80vh";
    }
    return false;
}

function editPostDragWindowleave(event) {
    if (editingPost){
        enterCounter--;
        if (enterCounter === 0) {
            var hiddenArea = document.getElementById("editPostDragHiddenArea_"+selectedPost);
            var dragArea = document.getElementById("postEditDragArea_"+selectedPost);
            dragArea.style.display = "none";
            hiddenArea.style.display = "block";
        }
    }else{
        enterCounter--;
        if (enterCounter === 0) {
            var dropZone = document.getElementById("dropZone");
            dropZone.style.height = "50px";
        }
    }
    return false;
}

function postEditInputClick( event ){
    var postID = event.target.id.split("_")[1];
    var files = event.target.files;
    showFilesInPostEdit(postID, files);
    sendFilesFromPostEdit(files);
    //alert(event.target.id);
}
function showFilesInPostEdit(postID, files){
    var uploadTable = document.getElementById("PostFilesTable_"+postID);
    for (var i = 0; i < files.length; i++){
        uploadTable.tBodies[0].appendChild(showFileInPostEdit(files[i]));
    }
    //calculateLen();
}

function showFileInPostEdit(file) {
    var fileRow = document.createElement("tr");
    fileRow.id = "postEditFileRow_" + fileCount;

    var td1 = document.createElement("td");
    td1.className = "FeedTdLeft";
    // var previewImg = document.createElement("img");
    // previewImg.setAttribute("src", "/resources/media/feeds/photofile.png");
    // td1.appendChild(previewImg);

    var td2 = document.createElement("td");
    td2.className = "FeedTdCenter";
    td2.title = file.name;
    td2.innerText = file.name;
    sliceTextForPostEdit(td2);

    var td3 = document.createElement("td");
    td3.innerText = formatSize(file.size);
    td3.className = "FeedTdRight";
    //progressStatuses.push(th3);

    var td4 = document.createElement("td");


    var img = document.createElement("img");
    img.id = "deleteTmpFileEdit_"+ fileCount;
    img.src = "/resources/media/feeds/donloadCancel.png";
    img.style.width = "15px";
    td4.appendChild(img);

    fileRow.appendChild(td1);
    fileRow.appendChild(td2);
    fileRow.appendChild(td3);
    fileRow.appendChild(td4);

    fileCount++;
    return fileRow;
}

function sendFilesFromPostEdit(files){
    for (var i = 0; i < files.length; i++) {
        var formData = new FormData();
        formData.append("file", files[i], files[i].name);

        $.ajax({
            url: document.URL + "/uploadFile?${_csrf.parameterName}=${_csrf.token}",
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
                reqUp.id = "postEditSendXHR_"+j;
                reqUp.addEventListener('progress', uploadProgressForPostEdit, false);
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

function uploadProgressForPostEdit(event) {
    var fileId = event.target.id.split("_")[1];
    var progressStatus = document.getElementById("postEditFileRow_"+fileId);
    var percent = parseInt(event.loaded / event.total * 100);
    progressStatus.style.background = "linear-gradient(90deg, #18ff00 " + percent + "%, #FFF " + percent +"%)";

    //progressStatuses[event.target.id].innerText = parseInt(event.loaded / event.total * 100) + "%";
    //if (progressStatuses[event.target.id].innerText === "100%")
    //    progressStatuses[event.target.id].style.color = "green";
}

function updatePostAfterEdit(event){
    event.preventDefault();
    var postID = event.target.id.split("_")[1];
    //if (filesToDelete.length !== 0) postEditSendFilesToDelete();
    postEditSendSaveInfo(postID);
}
function postEditSendSaveInfo(postID) {
    var comment = document.getElementById('postComment_'+postID);
    var obj = {
        comment: comment.innerText,
        feedName: "NULL",
        postID: postID,
        filesToDelete: filesToDelete
    };
    var data = JSON.stringify(obj);
    var docURL = document.URL + "/save";
    $.ajax({
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        url: docURL,
        method: 'POST',
        data: data,
        success: function(data){
            if (data.nullPost === false){
                var div = document.createElement('div');
                div.innerHTML = data.htmlPost;

                var mainCenter = document.getElementById("mainCenter");
                mainCenter.replaceChild(div.firstChild, document.getElementById("FeedContainer_"+postID));
                hidePostEdit(data.postId);
            }
            filesToDelete = [];
            //hideUploadBlock();
        },
        error: function (e) {
            alert("Error!!!\n" + e.responseText);
        }
    });
}
function postEditDeleteButtonClick( event ){
    var postID = event.target.id.split("_")[1];
    var deleteCheckboxes = document.getElementsByClassName("postEditFileDeleteCheckboxes_" + postID);
    var fileID;
    for (var i = 0; i < deleteCheckboxes.length; i++){
        if (deleteCheckboxes[i].checked === true){
            fileID = deleteCheckboxes[i].id.split("_")[1];
            filesToDelete.push(fileID);
            document.getElementById("postFileRaw_"+ fileID).style.backgroundColor = "red";
        }
    }
}

function postEditMainDeleteCheckboxClicked(event) {
    var postID = event.target.id.split("_")[1];
    var deleteCheckboxes = document.getElementsByClassName("postEditFileDeleteCheckboxes_" + postID);
    if (event.target.checked === true) {
        for (var i = 0; i < deleteCheckboxes.length; i++) {
            deleteCheckboxes[i].checked = true;
        }
    }else{
        for (var i = 0; i < deleteCheckboxes.length; i++) {
            deleteCheckboxes[i].checked = false;
        }
    }
}

// function postEditSendFilesToDelete() {
//     var data = filesToDelete;
//     $.ajax({
//         async: false,
//         url: document.URL + "/deleteFile",
//         method: 'POST',
//         data: {data:data},
//         success: function(data){
//
//         },
//         error: function (e) {
//             alert(e.responseText);
//         }
//     });
// }

// --------------------StopscScrolling--------------------

function disableScrolling(event){
    var x=window.scrollX;
    var y=window.scrollY;
    window.onscroll=function(){window.scrollTo(x, y);};


}

function enableScrolling(){
    window.onscroll=function(){};
}



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


function formatSize(length){
    var i = 0, type = ['б','Кб','Мб','Гб','Тб','Пб'];
    while((length / 1000 | 0) && i < type.length - 1) {
        length /= 1024;
        i++;
    }
    if (i>2)
        return length.toFixed(2) + ' ' + type[i];
    else
        return length.toFixed(0) + ' ' + type[i];
}
