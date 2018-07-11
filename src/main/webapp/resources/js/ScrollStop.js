var maxWitdhFeedtdCenter=365;
var maxWitdhThOne=260;
// ------------------------crop file name---------------
document.addEventListener('DOMContentLoaded', function(){

    // var text = document.getElementsByClassName("thOne");
    // for (var j = 0; j < text.length; j++) {
    //      sliceTextForUploadContainer(text[j]);
    // }

    var text = document.getElementsByClassName("FeedtdCenter");
    for (var j = 0; j < text.length; j++) {
        sliceTextForPostEdit(text[j]);
    }
});

function sliceTextForUploadContainer(elem) {
        var c = document.createElement("canvas");
        c.id = "myCanvas";
        c.style.width = "370";
        c.style.height = "51";
        var ctx = c.getContext("2d");
        ctx.font = "15px Arial";
        var txt = elem.innerText;
        if (ctx.measureText(txt).width < maxWitdhThOne){
            return;
        }
        var pos =txt.lastIndexOf(".");
        ext="..."+txt.substr(pos-2);
        base=txt.substr(0,pos-2);
        var sum = "";
        var lenb=base.length;
        for (var i = lenb; i > 0; i--){
            base=base.substr(0,i);
            //alert(base)
            sum=base+ext;
            if (ctx.measureText(sum).width < maxWitdhThOne){
                break;
            }
        }
        elem.innerText = base+ext;
}

function sliceTextForPostEdit(elem) {
        var c = document.createElement("canvas");
        c.id = "myCanvas";
        c.style.width = "370";
        c.style.height = "51";
        var ctx = c.getContext("2d");
        ctx.font = "16px Arial";
        var txt = elem.innerText;
        if (ctx.measureText(txt).width < maxWitdhFeedtdCenter){
            return;
        }
        var pos = txt.lastIndexOf(".");
        ext = "..." + txt.substr(pos - 2);
        base = txt.substr(0, pos - 2);

        var sum = "";

        var lenb = base.length;
        for (var i = lenb; i > 0; i--) {
            base = base.substr(0, i);
            sum = base + ext;
            if (ctx.measureText(sum).width < maxWitdhFeedtdCenter) {
                break;
            }
        }

        elem.innerText = base + ext;
}

// --------------------StopscScrolling--------------------


var scrollKeys = [33,34,35,36,38,40];
var popUpClass = ".uploadStatus";


function preventScroll() {
  $(document).on("mousewheel DOMMouseScroll", "body", function(e) {
    
    var $np = $(popUpClass)[0];
    var st = $np.scrollTop;
    if (e.originalEvent.wheelDelta > 0 || e.originalEvent.detail < 0) {
        if ($(e.target).closest(popUpClass).length && 
            st > 0) return;
    } else {
        if ($(e.target).closest(popUpClass).length && 
            st + $np.offsetHeight < $np.scrollHeight) return;
    }
    e.preventDefault();
    e.stopPropagation();
    
  });

$(document).on("keydown", function(e) {
    if (scrollKeys.indexOf(e.which) > -1) {
      e.preventDefault();
    }
  });
};

var popUpClassTheNote = ".comment";
  function preventScrollTheNote() {
  $(document).on("mousewheel DOMMouseScroll", "body", function(e) {
    
    var $np = $(popUpClassTheNote)[0];
    var st = $np.scrollTop;
    if (e.originalEvent.wheelDelta > 0 || e.originalEvent.detail < 0) {
        if ($(e.target).closest(popUpClassTheNote).length && 
            st > 0) return;
    } else {
        if ($(e.target).closest(popUpClassTheNote).length && 
            st + $np.offsetHeight < $np.scrollHeight) return;
    }
    e.preventDefault();
    e.stopPropagation();
    
  });


    $(document).on("keydown", function(e) {
    if (scrollKeys.indexOf(e.which) > -1) {
      e.preventDefault();
    }
  });
};

  

function returnScroll() {
  $(document).off("mousewheel DOMMouseScroll keydown");
};


// ---------------------more about the note------------------------

var LimitLines = 10;

function mathLines(mode, postId){
var ele = document.getElementById("postComment_"+postId);
var text = document.getElementById("hideButton_"+postId);
var colSymbol = ele.lastChild.length;
var divHeidht = ele.clientHeight;
var lines = colSymbol/70;

var spoiler = document.getElementById("CommentPostGrad_"+postId);

if (mode==0){
    if (lines > LimitLines){
        text.style.display = "flex";
        ele.style.height = LimitLines*20 +"px";
    }
    else {
        text.style.display = "none";
        spoiler.style.visibility = "hidden";
    }
}

else{
    if (divHeidht == LimitLines*20 ){
        text.innerHTML = "Скрыть заметку"
        ele.style.height = "auto";
        spoiler.style.visibility = "hidden";
    }
    else {
        text.innerHTML = "Показать заметку полностью"
        ele.style.height = LimitLines*20 +"px";
        spoiler.style.visibility = "visible";
    }
}  
}

// document.addEventListener('DOMContentLoaded',function() {
// mathLines(0);
//
// })

function MoreInfo(event) {
    mathLines(1, event.target.id.split("_")[1]);
 }


// ---------------------------DeleteBorder-------------------

document.addEventListener('DOMContentLoaded',function() {

    var DeleteBorder = document.getElementsByClassName("DeleteBorder");
    var BackgroundNone = document.getElementsByClassName("BackgroundNone");
    for (var i = 0; i < DeleteBorder.length; i++){
        DeleteBorder[i].style.borderTop = "1px";
    }
    for (var j = 0; j < BackgroundNone.length; j++){
        BackgroundNone[j].style.background = "white";
    }
   
});

