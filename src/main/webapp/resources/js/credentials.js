var CREDENTIALS_SELECTOR = {
    SAVE_CREDENTIALS_BUTTON: ".jsSaveCredentials",
    CLEAR_CREDENTIALS_BUTTON: ".jsClearCredentials",
    CREDENTIALS_FIELDS: ".jsCredentials",
};

var CLASS = {
    HIDDEN: "_hidden",
};

$(document).ready(function () {
    initCredentials();
    $(CREDENTIALS_SELECTOR.SAVE_CREDENTIALS_BUTTON).on('click', onSaveClick);
    $(CREDENTIALS_SELECTOR.CLEAR_CREDENTIALS_BUTTON).on('click', onClearClick);

    function initCredentials() {
        $.ajax({
            url: CONSTANT.URL + "/credentials/isCredentialsSetted",
            type: "GET",
            success: function (data, status, xhr) {
                var isCredentialsSetted = data.result,
                    $saveButton = $(CREDENTIALS_SELECTOR.SAVE_CREDENTIALS_BUTTON),
                    $clearButton = $(CREDENTIALS_SELECTOR.CLEAR_CREDENTIALS_BUTTON),
                    $credentialsFields = $(CREDENTIALS_SELECTOR.CREDENTIALS_FIELDS);
                if (isCredentialsSetted){
                    $saveButton.addClass(CLASS.HIDDEN);
                    $clearButton.removeClass(CLASS.HIDDEN);
                    $credentialsFields.prop("disabled", true);
                } else {
                    $clearButton.addClass(CLASS.HIDDEN);
                    $saveButton.removeClass(CLASS.HIDDEN);
                    $credentialsFields.prop("disabled", false);
                }
            },
            error: function (xhr, status, error) {
                onError(xhr.responseJSON.error);
            }
        });
    }

    function onSaveClick() {
        var data = {};
        if (areCredentialsExists()){
            data.credentials = getCredentials();
            $.ajax({
                url: CONSTANT.URL + "/credentials/set",
                type: "POST",
                data: JSON.stringify(data),
                contentType: "application/json",
                success: function (data, status, xhr) {
                    onSuccess(data.result);
                    $(CREDENTIALS_SELECTOR.SAVE_CREDENTIALS_BUTTON).addClass(CLASS.HIDDEN);
                    $(CREDENTIALS_SELECTOR.CLEAR_CREDENTIALS_BUTTON).removeClass(CLASS.HIDDEN);
                    $(CREDENTIALS_SELECTOR.CREDENTIALS_FIELDS).prop("disabled", true);
                },
                error: function (xhr, status, error) {
                    onError(xhr.responseJSON.error);
                }
            });
        } else {
            onError("Fill credentials fields !")
        }

    }

    function onClearClick() {
        $.ajax({
            url: CONSTANT.URL + "/credentials/clear",
            type: "DELETE",
            success: function (data, status, xhr) {
                onSuccess(data.result);
                $(CREDENTIALS_SELECTOR.CLEAR_CREDENTIALS_BUTTON).addClass(CLASS.HIDDEN);
                $(CREDENTIALS_SELECTOR.SAVE_CREDENTIALS_BUTTON).removeClass(CLASS.HIDDEN);
                $(CREDENTIALS_SELECTOR.CREDENTIALS_FIELDS).prop("disabled", false);
            },
            error: function (xhr, status, error) {
                onError(xhr.responseJSON.error);
            }
        });
    }

    function getCredentials() {
        var login = $(SELECTOR.LOGIN).val();
        var password = $(SELECTOR.PASSWORD).val();
        return login + ":" + password;
    }

    function areCredentialsExists() {
        var login = $(SELECTOR.LOGIN).val();
        var password = $(SELECTOR.PASSWORD).val();

        return !!(login && password);
    }
});