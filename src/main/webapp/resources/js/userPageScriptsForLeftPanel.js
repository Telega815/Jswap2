var selectedFeed;
$(document).ready(function(){
    var feeds = document.getElementsByClassName("feeds");
    for (var i=0;i<feeds.length;i++){
        feeds[i].onclick=getPostsOfFeed;
    }
    if (feeds.length !== 0){
        feeds[0].click();
        selectedFeed = feeds[0].id.split("_")[1];
    }

    //trueSizeFreeSpace
    var spaceBizy = document.getElementById("leftPanelUserFilesSize");
    var spaceFree = document.getElementById("leftPanelUserSizeLimit");
    spaceBizy.innerText=(spaceBizy.innerText/1024/1024/1024).toFixed(2);
    spaceFree.innerText=spaceFree.innerText/1024/1024/1024;

    //trueSizeSpaceOfFeeds
    var fileSizeFeeds = document.getElementsByClassName("tdRight");
    for(i = 0; i <fileSizeFeeds.length; i++) {
        if (fileSizeFeeds[i].innerText!=="0.0") {
            fileSizeFeeds[i].innerText = "Занято " + formatSize(fileSizeFeeds[i].innerText);
        }
        else{
            fileSizeFeeds[i].innerText = "Занято 0 Байт";
        }
    }
});

//TODO format size not working correctly (need contain somewhere feed sizes in bytes)
function changeFeedSize(feedId, newSize) {
    var td = document.getElementById("feedTdRight_"+feedId);
    td.innerText = formatSize(newSize);
}

function getPostsOfFeed(event) {
    var id = event.target.id.split('_')[1];
    selectedFeed = id;
    var formData = new FormData();
    formData.append("feedId", id);
    $.ajax({
        url: window.location.protocol +"//"+window.location.host+"/restService/getPostsOfFeed?"+csrfParameter+"="+csrfToken,
        enctype: "multipart/form-data",
        method: "POST",
        data: formData,
        cache: false,
        contentType: false,
        processData: false,
        success: function (data) {
                if (data==="Pincode required" || data==="Authentication required" || data==="Undefined behavior"){
                    alert(data)
                }
                else {
                    document.getElementById("mainCenter").innerHTML = data;
                    hideEditOfAllPosts();
                    formatSizesOfAllPosts();
                    filesClickInit();
                }
        },
        error: function (e) {
            alert(e.responseText);
        }
    })
}