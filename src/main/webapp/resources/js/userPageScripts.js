var optionBlocks;
var spoilerButtons;
var spoilerGradient;
var textBlocks;
var deleteRows;
var fileCheckBoxes;
var inputFileFields;
var saveButtons;
var selectedPost;
var editingPost = false;
var fileTables;

// $(document).ready(function(){
//
//     fileTables = document.getElementsByClassName("FeedFolders");
//     spoilerButtons = document.getElementsByClassName("LinkSpoiler");
//     spoilerGradient = document.getElementsByClassName("CommentPostGrad");
//     optionBlocks = document.getElementsByClassName("ediFeedOptions");
//     textBlocks = document.getElementsByClassName("commentPost");
//     deleteRows = document.getElementsByClassName("deleteRow");
//     fileCheckBoxes = document.getElementsByClassName("FeedTdDeleteEdit");
//     inputFileFields = document.getElementsByClassName("uploadDad");
//     saveButtons = document.getElementsByClassName("SaveButton");
//
//     hideEdit();
//
//
// });
//
// function hideEdit() {
//     for (var i = 0; i < deleteRows.length; i++){
//         deleteRows[i].hidden = true;
//
//         inputFileFields[i].style.display = "none";
//         inputFileFields[i].style.display = "none";
//         saveButtons[i].style.display = "none";
//         if (textBlocks[i].innerText !== ""){
//             mathLines(0, spoilerButtons[i].id.split("_")[1]);
//         }else{
//             spoilerButtons[i].style.display = "none";
//             spoilerGradient[i].style.display = "none";
//         }
//     }
//     for (i = 0; i < fileCheckBoxes.length; i++){
//         fileCheckBoxes[i].hidden = true;
//     }
// }



function hidePostEdit(postId){
    var delRow = document.getElementById("deleteRow_"+postId);
    var firstFileRow = document.getElementsByClassName("postFileRaws_"+postId)[0];
    var delCheckBoxes = document.getElementsByClassName("FeedTdDeleteEdit_"+postId);
    var uploadDad = document.getElementById("uploadDad_"+postId);
    var saveDiv = document.getElementById("SaveButton_"+postId);
    var postComment = document.getElementById("postComment_"+postId);
    var postDownloadImgs = document.getElementsByClassName("FeedTdDownload_"+postId);
    var spoilerButton = document.getElementById("hideButton_"+postId);
    var postGrad = document.getElementById("CommentPostGrad_"+postId);
    var postFilesNames = document.getElementsByClassName("PostTdCenter_"+postId);

    if (postComment.innerText.length !== 0){
        mathLines(0, postId);
        postComment.setAttribute("contenteditable", "false");
        postComment.classList.remove("CommentPostConten");
    }else{
        spoilerButton.style.display = "none";
        postGrad.style.display = "none";
    }

    firstFileRow.style.borderTop = "1px";
    delRow.hidden = true;
    uploadDad.style.display = "none";
    saveDiv.style.display = "none";
    for (var i = 0; i <delCheckBoxes.length; i++){
        delCheckBoxes[i].hidden = true;
        postDownloadImgs[i].hidden = false;
        sliceTextForPostEdit(postFilesNames[i]);
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

