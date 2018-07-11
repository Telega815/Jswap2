
var postId = 0;
var fileNames = [];
var namesID = -1;
var namesCount = 0;
var progressBars = [];
var j = 0;

$(document).ready(function() {
    var postId = 0;
    var dropZone = $('#dropZone');
    dropZone[0].ondrop = function(event) {
        event.preventDefault();
        document.getElementById('dropZone').style.backgroundColor = "#545b5f";
        var files = event.dataTransfer.files;
        showFiles2(files);
        for (var i = 0; i < files.length; i++) {
            var formData = new FormData();
            formData.append("file", files[i], files[i].name);

            $.ajax({
                url: document.URL + "/" + postId + "/uploadFile?${_csrf.parameterName}=${_csrf.token}",
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
                    reqUp.id =j.toString();
                    reqUp.addEventListener('progress', uploadProgress, false);
                    j++;
                    return req;
                },
                success: function (data) {
                    postId = data;
                },
                error: function (e) {
                    alert(e.responseText);
                }
            });
        }
    }
    dropZone[0].ondragover = function() {
        document.getElementById('dropZone').style.backgroundColor = 'gray';
        return false;
    };

    dropZone[0].ondragleave = function() {
        document.getElementById('dropZone').style.backgroundColor = "#545b5f";
        return false;
    };
});

function showShit(event){
    var files = event.target.files;
    alert(files);
}

function sendFilesSeparatly(event) {
    showFiles2(event.target.files);
    var files = event.target.files;
    for (var i = 0; i < files.length; i++) {
        var formData = new FormData();
        formData.append("file", files[i], files[i].name);

        $.ajax({
            url: document.URL + "/" + postId + "/uploadFile?${_csrf.parameterName}=${_csrf.token}",
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
                reqUp.id =j.toString();
                reqUp.addEventListener('progress', uploadProgress, false);
                j++;
                return req;
            },
            success: function (data) {
                postId = data;
            },
            error: function (e) {
                alert(e.responseText);
            }
        });
    }

}
function uploadProgress(event) {
    progressBars[event.target.id].value = parseInt(event.loaded / event.total * 100);
}


function sendFile(file) {
    var uri = document.URL + "/" + postId + "/uploadFile?${_csrf.parameterName}=${_csrf.token}";
    var xhr = new XMLHttpRequest();
    var fd;
    fd = new FormData();
    // fd.append("username", "Groucho");
    // fd.append("accountnum", 123456);

    xhr.open("POST", uri, true);
    xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
    xhr.onreadystatechange = function() {
        if (xhr.readyState == 4 && xhr.status == 200) {
            // Handle response.
            alert(xhr.responseText); // handle response.
        }
    };
    fd.append('myFile', file);
    alert(fd);
    // alert(fd.getAll('username')[0]);
    // Initiate a multipart/form-data upload
    xhr.send(fd);
}

function showFiles2(files) {
    var fileList = document.getElementById('fileList');
    for (var i = 0; i < files.length; i++) {
        var li = document.createElement('li');
        var liDelete = document.createElement('li');

        var liProgress = document.createElement('li');
        var progressBar = document.createElement('progress');
        progressBar.value = 0;
        progressBar.max = 100;
        progressBar.setAttribute("class", "ProgressBar");
        liProgress.appendChild(progressBar);
        progressBars.push(progressBar);

        var k = 2;
        var checkingName = files[i].name;
        while (checkName2(checkingName)){
            checkingName = files[i].name;
            checkingName += "-"+k;
            k++;
        }
        fileNames.push(checkingName);
        namesID++;namesCount++;
        li.innerHTML = checkingName;
        li.setAttribute('id', ("name-" + (namesID)));
        liDelete.innerHTML = createDeleteBtn2(namesID).outerHTML;
        fileList.appendChild(li);
        fileList.appendChild(liDelete);
        fileList.appendChild(liProgress);
    }
}
function checkName2(name) {
    for (var i = 0; i < fileNames.length; i++) {
        if (fileNames[i] === name){
            return true;
        }
    }
    return false;
}
function createDeleteBtn2(id) {
    var deleteButton = document.createElement('button');

    var idNode = document.createAttribute('id');
    idNode.value = "delete-" + id;
    deleteButton.setAttributeNode(idNode);

    var valueNode = document.createAttribute('value');
    valueNode.value = "delete file";
    deleteButton.setAttributeNode(valueNode);

    var onclick = document.createAttribute('onclick');
    onclick.value = "deleteFile(event)";
    deleteButton.setAttributeNode(onclick);
    return deleteButton;
}

function sendFiles() {
    var progressBar = $('#progressbar');
    var form = document.forms[0];
    var input = document.getElementById('files');
    var formData = new FormData(form);
    $.ajax({
        url: document.URL + "/" + postId + "/upload?${_csrf.parameterName}=${_csrf.token}",
        enctype: "multipart/form-data",
        method: 'POST',
        type: 'POST',
        data: formData,
        cache: false,
        contentType: false,
        processData: false,
        xhr: function(){
            var xhr = $.ajaxSettings.xhr(); // получаем объект XMLHttpRequest
            xhr.upload.addEventListener('progress', function(evt){ // добавляем обработчик события progress (onprogress)
                if(evt.lengthComputable) { // если известно количество байт
                    // высчитываем процент загруженного
                    var percentComplete = Math.ceil(evt.loaded / evt.total * 100);
                    // устанавливаем значение в атрибут value тега <progress>
                    // и это же значение альтернативным текстом для браузеров, не поддерживающих <progress>
                    progressBar.val(percentComplete).text('Загружено ' + percentComplete + '%');
                }
            }, false);
            return xhr;
        },
        success: function (data) {
            postId = data;
            showFiles(input)
        },
        error: function (e) {
            alert(e.responseText);
        }
    });

    function showFiles(input) {
        var files = input.files;
        var fileList = document.getElementById('fileList');
        for (var i = 0; i < files.length; i++) {
            var li = document.createElement('li');
            var liDelete = document.createElement('li');

            var liProgress = document.createElement('li');
            var progressBar = document.createElement('progress');
            progressBar.value = 0;
            progressBar.max = 100;
            progressBar.class = "ProgressBar";
            liProgress.appendChild(progressBar);

            var k = 2;
            var checkingName = files[i].name;
            while (checkName(checkingName)){
                checkingName = files[i].name;
                checkingName += "-"+k;
                k++;
            }
            fileNames.push(checkingName);
            namesID++;namesCount++;
            li.innerHTML = checkingName;
            li.setAttribute('id', ("name-" + (namesID)));
            liDelete.innerHTML = createDeleteBtn(namesID).outerHTML;
            fileList.appendChild(li);
            fileList.appendChild(liDelete);
            fileList.appendChild(liProgress);
        }
    }
    function checkName(name) {
        for (var i = 0; i < fileNames.length; i++) {
            if (fileNames[i] === name){
                return true;
            }
        }
        return false;
    }
    function createDeleteBtn(id) {
        var deleteButton = document.createElement('button');

        var idNode = document.createAttribute('id');
        idNode.value = "delete-" + id;
        deleteButton.setAttributeNode(idNode);

        var valueNode = document.createAttribute('value');
        valueNode.value = "delete file";
        deleteButton.setAttributeNode(valueNode);

        var onclick = document.createAttribute('onclick');
        onclick.value = "deleteFile(event)";
        deleteButton.setAttributeNode(onclick);
        return deleteButton;
    }
}

function deleteFile( event ) {
    var fileList = document.getElementById('fileList');
    var idNum = event.target.id.substring(7);
    var deleteUrl = document.URL + "/delete/" + postId + "/" + fileNames[idNum];
    $.ajax({
        url: deleteUrl,
        type: 'GET',
        success: function () {
            var deleteButton = document.getElementById('delete-'+idNum);
            var li = document.getElementById('name-'+idNum);
            delete fileNames[idNum];
            fileList.removeChild(deleteButton.parentNode);
            fileList.removeChild(li);
            namesCount--;
            if (namesCount === 0) postId = 0;
        },
        error: function (e) {
            alert(e.responseText);
        }
    });
}

function saveFiles() {
    var comment = document.getElementById('uploadComment');
    var feedname = document.getElementById('selectFeed').value;
    // var formData = new FormData();
    // formData.append("comment", comment.value);
    var obj = {
        comment: comment.value,
        feedName: feedname
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
            alert(data);
        },
        error: function (e) {
            alert(e.responseText);
        }
    });
}
