var CONSTANT = {
    URL: 'http://localhost:8090',
};

var SELECTOR = {
    RESULT_TABLE: '.jsResultTable',
    TITLE: '.jsTitle'
};

var URL_PARAMS_KEYS = {
    GLOBAL: "objectGlobal",
};

var URL_PARAMS_VALUES = {
    COURSE: 'course',
    REPOSITORY: 'repository'
};

function initDOM(){
    var params = new URL(window.location.href).searchParams;
    switch (params.get(URL_PARAMS_KEYS.GLOBAL)){
        case URL_PARAMS_VALUES.COURSE:
            $(SELECTOR.TITLE).html("<h1>Course name</h1>");
            break;
        case URL_PARAMS_VALUES.REPOSITORY:
            $(SELECTOR.TITLE).html("<h1>Repository name</h1>");
            break;
        //Other...

        default:
    }
}


$(document).ready(function() {

    initDOM();
});