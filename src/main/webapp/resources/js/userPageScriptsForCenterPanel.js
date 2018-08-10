var enterCounter = 0;
var selectedPost;
var editingPost = false;
var fileCouterForPostEdit = 0;
var editPostUploadedFiles = [];
var editPostDeletingFiles = [];

//---------------------innerDZ-------------------
$(document).ready(function(){
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

    // -----------------------------ctrl & Shift -------------------
    var body = document.body;

    $("body").click(bodyClick);



});

// for edit post----------------------------------------------------------------------
function filesClickInit() {
    var posts = document.getElementsByClassName("FeedContainer");
    for (var i = 0; i < posts.length; i++){
        filesClickInitForOnePost(posts[i].id.split("_")[1]);
    }
}
function filesClickInitForOnePost(postId) {
    var select_start_index = -1;
    jQuery("#editPostDragHiddenArea_"+postId).on('click', '.FeedFolders tbody tr', function ctrlShift(e) {

        var t_rows = $(this).parent().find('tr');


        if (!e.shiftKey && !e.ctrlKey) {
            var fakeEvent = {
                target:document.body
            };
            bodyClick(fakeEvent);

            $('.active').children().children(3).removeAttr("checked");
            t_rows.removeClass('active');
            $(this).addClass('active');
            $('.active').children().children(3).attr("checked", "checked");


            select_start_index = t_rows.index(this);
        }

        if (e.ctrlKey) {
            // console.log('ctrl');

            if ($(this).hasClass('active')) {
                $(this).children().children(3).removeAttr("checked");
                $(this).removeClass('active');

                // console.log("sdfg");
            } else {
                $(this).addClass('active');
                $('.active').children().children(3).attr("checked", "checked");


            }
        }

        if (e.shiftKey) {
            var select_end_index = t_rows.index(this);
            var each_start, each_end;
            if(select_start_index != -1){
                // console.log('shift');
                if(select_start_index < select_end_index){
                    each_start = select_start_index;
                    each_end = select_end_index;
                }
                else{
                    each_start = select_end_index;
                    each_end = select_start_index;
                }
                t_rows.each(function(index){
                    if(index >= each_start && index <= each_end){
                        $(this).addClass('active');
                        $('.active').children().children(3).attr("checked", "checked");
                    }
                });
            }

        }

    });


}

function bodyClick(e) {
    if (e.target.type === "submit"||$(e.target).hasClass('FeedFolders')||$(e.target).parents().hasClass('FeedFolders')) {

        return false;
    }
    else{
        $('.FeedTdDeleteEdit').children().removeAttr('checked');
        $('.active').removeClass('active');
    }
}

function hidePostEdit(postId){
    var delRow = document.getElementById("ButtonsPostEditDeleteSelectAll_"+postId);
    var firstFileRow = document.getElementsByClassName("postFileRaws_"+postId)[0];
    var delCheckBoxes = document.getElementsByClassName("FeedTdDeleteEdit_"+postId);
    var uploadDad = document.getElementById("uploadDad_"+postId);
    var saveDiv = document.getElementById("SaveButton_"+postId);
    var postComment = document.getElementById("postComment_"+postId);
    var postDownloadImgs = document.getElementsByClassName("FeedTdDownload_"+postId);
    var spoilerButton = document.getElementById("hideButton_"+postId);
    var postGrad = document.getElementById("CommentPostGrad_"+postId);
    var postFilesNames = document.getElementsByClassName("PostTdCenter_"+postId);

    postComment.style.height = "auto";
    if (postComment.innerText.length !== 0){
        mathLines(0, postId);
    }else{
        spoilerButton.style.display = "none";
        postGrad.style.display = "none";
    }
    postComment.setAttribute("contenteditable", "false");
    postComment.classList.remove("CommentPostConten");

    firstFileRow.style.borderTop = "1px";
    //delRow.hidden = true;
    delRow.style.display = "none";
    uploadDad.style.display = "none";
    saveDiv.style.display = "none";
    for (var i = 0; i <delCheckBoxes.length; i++){
        delCheckBoxes[i].hidden = true;
        postDownloadImgs[i].hidden = false;
        sliceTextForPostEdit(postFilesNames[i]);
    }
}


function editPostDrop(event) {
    event.preventDefault();
    var postID = event.target.id.split("_")[1];
    event.target.style.backgroundColor = "transparent";
    var files = event.dataTransfer.files;
    showFilesInPostEdit(postID, files);
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
}
function showFilesInPostEdit(postID, files){
    var uploadTable = document.getElementById("PostFilesTable_"+postID);
    for (var i = 0; i < files.length; i++){
        while(document.getElementById("postFileRaw_"+fileCouterForPostEdit) !== null){
            fileCouterForPostEdit++;
        }
        uploadTable.tBodies[0].appendChild(showFileInPostEdit(postID, files[i], fileCouterForPostEdit));
        sendFilesFromPostEdit(postID, files[i], fileCouterForPostEdit);
        editPostUploadedFiles.push(fileCouterForPostEdit);
    }
}

function showFileInPostEdit(postId, file, fileId) {
    var fileRow = document.createElement("tr");
    fileRow.id = "postFileRaw_" + fileId;
    var td1 = document.createElement("td");
    td1.className = "FeedTdLeft tempEditRow";

    var td2 = document.createElement("td");
    td2.className = "FeedTdCenter tempEditRow";
    td2.title = file.name;
    td2.innerText = file.name;
    sliceTextForPostEdit(td2);

    var td3 = document.createElement("td");
    td3.innerText = formatSize(file.size);
    td3.className = "FeedTdRight tempEditRow";
    //progressStatuses.push(th3);

    var td4 = document.createElement("td");

    td4.className = "FeedTdDeleteEdit tempEditRow FeedTdDeleteEdit_"+ fileId;

    var checkbox = document.createElement("input");
    checkbox.id = "postEditFileDeleteCheckbox_"+ fileId;
    checkbox.classList.add("postEditFileDeleteCheckboxes_"+ postId);
    checkbox.classList.add("tempEditRow");
    checkbox.setAttribute("type", "checkbox");
    td4.appendChild(checkbox);

    fileRow.appendChild(td1);
    fileRow.appendChild(td2);
    fileRow.appendChild(td3);
    fileRow.appendChild(td4);

    return fileRow;
}

//TODO some shit here i donno
function sendFilesFromPostEdit(postId, file, fileId){
    var formData = new FormData();
    formData.append("postId", postId);
    formData.append("file", file, file.name);
    formData.append("fileId", fileId);

    $.ajax({
        url: window.location.protocol + "//" + window.location.host + "/restService/uploadFileToExistingPost?"+csrfParameter+"="+csrfToken,
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
            reqUp.id = "xhr_"+fileId;
            reqUp.addEventListener('progress', uploadProgressForPostEdit, false);
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

function uploadProgressForPostEdit(event) {
    var fileId = event.target.id.split("_")[1];
    var progressStatus = document.getElementById("postFileRaw_"+fileId);
    var percent = parseInt(event.loaded / event.total * 100);
    progressStatus.style.background = "linear-gradient(90deg, #18ff00 " + percent + "%, #FFF " + percent +"%)";
}

function updatePostAfterEdit(event){
    event.preventDefault();
    var postID = event.target.id.split("_")[1];
    //if (filesToDelete.length !== 0) postEditSendFilesToDelete();
    postEditSendSaveInfo(postID);
}

//TODO this wasn't tested!!!
function postEditSendSaveInfo(postID) {
    var comment = document.getElementById('postComment_'+postID);
    var deleteCheckboxes = document.getElementsByClassName("postEditFileDeleteCheckboxes_" + postID);
    var fileID;
    var filesToSave = [];
    var filesToDelete = [];
    for (var i = 0; i < deleteCheckboxes.length; i++){
        fileID = deleteCheckboxes[i].id.split("_")[1];
        if (deleteCheckboxes[i].checked === true){
            if(editPostUploadedFiles.contains(fileID)) editPostUploadedFiles
            filesToDelete.push(fileID);
        }else{
            filesToSave.push(fileID);
        }
    }
    var obj = {
        comment: comment.innerText,
        postID: postID,
        filesToDelete: filesToDelete,
        filesToSave: filesToSave
    };

    var data = JSON.stringify(obj);
    var docURL = window.location.protocol + "//" + window.location.host + "/restService/updateExistingPost?"+csrfParameter+"="+csrfToken;
    $.ajax({
        headers: {
            'Content-Type': 'application/json'
        },
        url: docURL,
        method: 'POST',
        data: data,
        success: function(data){

        },
        error: function (e) {
            alert("Error!!!\n" + e.responseText);
        }
    });
}


function postEditDeleteButtonClick( event ){
    event.preventDefault();
    editPostDeletingFiles = [];
    var postID = event.target.id.split("_")[1];
    var deleteCheckboxes = document.getElementsByClassName("postEditFileDeleteCheckboxes_" + postID);
    var fileID;
    for (var i = 0; i < deleteCheckboxes.length; i++){
        if (deleteCheckboxes[i].checked === true){
            fileID = deleteCheckboxes[i].id.split("_")[1];
            if (!editPostDeletingFiles.contains(fileID)) {
                editPostDeletingFiles.push(fileID);
                document.getElementById("postFileRaw_"+ fileID).style.backgroundColor = "red";
            }
        }
    }
}

function postEditMainDeleteCheckboxClicked(event) {
    event.preventDefault();
    var postID = event.target.id.split("_")[1];
    var deleteCheckboxes = document.getElementsByClassName("postEditFileDeleteCheckboxes_" + postID);
    for (var i = 0; i < deleteCheckboxes.length; i++) {
        deleteCheckboxes[i].setAttribute("checked", "checked");
        deleteCheckboxes[i].parentElement.parentElement.classList.add('active');
    }
}

function postEditCancelClicked(event) {
    event.preventDefault();
    var postID = event.target.id.split("_")[1];
    var deleteCheckboxes = document.getElementsByClassName("postEditFileDeleteCheckboxes_" + postID);
    var i = 0;
    while (i < deleteCheckboxes.length) {
        var fileID = deleteCheckboxes[i].id.split("_")[1];
        deleteCheckboxes[i].removeAttribute("checked");
        if (deleteCheckboxes[i].classList.contains("tempEditRow")){
            var tempRow = document.getElementById("postFileRaw_"+ fileID);
            tempRow.parentNode.removeChild(tempRow);
        }else{
            document.getElementById("postFileRaw_"+ fileID).style.backgroundColor = "transparent";
            i++;
        }
    }
    hidePostEdit(postID);
    //TODO inform back-end about canceling
}

// hide edit of all posts in current feed ---------------------------------------------------------------
function hideEditOfAllPosts() {
    var posts = document.getElementsByClassName("FeedContainer");
    for(var i = 0; i < posts.length; i++){
        var postId = posts[i].id.split("_")[1];
        hidePostEdit(postId);
    }
}

// format sizes in all posts --------------------------------------------------------------------------------
function formatSizesOfAllPosts() {
    var fileSizeNodes = document.getElementsByClassName("FeedContainer");
    for (var i = 0; i < fileSizeNodes.length; i++){
        var postId = fileSizeNodes[i].id.split("_")[1];
        formatSizesForPost(postId);
    }
}

// format sizes of all files of post
function formatSizesForPost(postId) {
    var fileSizeNodes = document.getElementsByClassName("FeedtdRight_"+postId);
    for(var i=0; i <fileSizeNodes.length; i++) {
        fileSizeNodes[i].innerText=formatSize(fileSizeNodes[i].innerText);
    }
}



function optionsAction(event) {
    selectedPost = event.target.id.split("_")[1];
    var text = event.target.innerText;
    switch (text){
        case "Edit":
            var deleteRow = document.getElementById("ButtonsPostEditDeleteSelectAll_"+selectedPost);
            var postFileCheckBoxes = document.getElementsByClassName("FeedTdDeleteEdit_"+selectedPost);
            var postDownloadImgs = document.getElementsByClassName("FeedTdDownload_"+selectedPost);
            var postGrad = document.getElementById("CommentPostGrad_"+selectedPost);
            var uploadDad = document.getElementById("uploadDad_"+selectedPost);
            var dragArea = document.getElementById("postEditDragArea_"+selectedPost);
            for (var i = 0; i < postDownloadImgs.length; i++){
                postFileCheckBoxes[i].hidden = false;
                postDownloadImgs[i].hidden = true;
            }
            if (document.getElementById("postComment_"+selectedPost).innerText !== ""){
                mathLines(1, selectedPost);
            }
            document.getElementById("postComment_"+selectedPost).setAttribute("contenteditable", "true");
            document.getElementById("postComment_"+selectedPost).className += " CommentPostConten";
            postGrad.style.display ="none";
            //deleteRow.hidden = false;\
            deleteRow.style.display = "flex";
            deleteRow.style.borderTop = "1px";
            document.getElementById("hideButton_"+selectedPost).style.display = "none";
            document.getElementById("SaveButton_"+selectedPost).style.display = "flex";
            uploadDad.style.display = "flex";
            dragArea.ondrop = editPostDrop;
            dragArea.ondragover = editPostDragover;
            dragArea.ondragleave = editPostDragleave;
            editingPost = true;
            break;
        case "Delete":
            var formData = new FormData();
            formData.append("postId", selectedPost);
            $.ajax({
                headers: {
                    'Accept': 'application/json'
                },
                url: window.location.protocol +"//"+window.location.host+"/restService/deletePost?"+csrfParameter+"="+csrfToken,
                type: "POST",
                enctype: "multipart/form-data",
                method: "POST",
                data: formData,
                cache: false,
                contentType: false,
                processData: false,
                success: function (data) {
                    if (!data.nullPost){
                        document.getElementById("mainCenter").removeChild(document.getElementById("FeedContainer_"+selectedPost));

                        var tdRight = document.getElementsByClassName("tdRight");
                        //UpdateFeedAndUserSpace
                        for (var i=0; i<tdRight.length;i++){

                            if (tdRight[i].id.split("_")[1]==selectedFeed){
                                tdRight[i].innerText = "Занято " + formatSize(data.feedSize);

                            }
                        }
                        document.getElementById("leftPanelUserFilesSize").innerText = (data.userSpace/1024/1024/1024).toFixed(2);
                        updateUserSpace();
                    }

                },
                error: function (e) {
                    alert(e.responseText);
                }

            })
    }
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


