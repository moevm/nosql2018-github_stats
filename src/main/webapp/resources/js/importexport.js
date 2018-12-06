function getDump() {
    window.location = CONSTANT.URL + "/backup"
}

function loadDumpToServer() {
    let file = $(SELECTOR.FILE_CHOOSER)[0].files[0];
    let dropOld = $(SELECTOR.DROP_OLD).is(':checked');
    if (file === undefined) {
        onError("Choose file")
    } else {
        var formData = new FormData();
        formData.append('file', file);
        formData.append('dropOld', dropOld);
        $.ajax({
            url: CONSTANT.URL + "/restore",
            data: formData,
            type: 'POST',
            contentType: false,
            processData: false,
            success: function (result, status, xhr) {
                redirectToMain()
            },
            error: function (xhr, status, error) {
                onError(error);
            }
        });
    }
}