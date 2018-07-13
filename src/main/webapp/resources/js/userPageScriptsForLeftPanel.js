
$(document).ready(function(){
    var feeds = document.getElementsByClassName("feeds");
    for (var i=0;i<feeds.length;i++){
        feeds[i].onclick=getPostsOfFeeds;
    }
    if (feeds.length !== 0){
        feeds[0].click();
    }
});

function getPostsOfFeeds(event) {
    var id = event.target.id.split('_')[1];
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
                if (data=="Pincode Requared" || data=="Autentification Requared" || data=="Undefined behavior"){
                    alert(data)
                }
                else {
                    document.getElementById("mainCenter").innerHTML = data;
                }
        },
        error: function (e) {
            alert(e.responseText);
        }
    })
}