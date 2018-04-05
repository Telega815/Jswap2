
var postId = "next";
var fileNames = [];
var namesID = -1;
var namesCount = 0;

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
            if (namesCount === 0) postId = "next";
        },
        error: function (e) {
            alert(e.responseText);
        }
    });
}

function saveFiles() {
        $.ajax({
        url: document.URL + "/upload/save",
        type: 'POST',
        data: postId,
        success: alert("success"),
        error: function (e) {
            alert(e.responseText);
        }
    });
}
