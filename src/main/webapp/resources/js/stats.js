var SELECTOR = {
    RESULT_TABLE: '.jsResultTable',
    TITLE: '.jsTitle'
};

function initDOM(){
    var params = new URL(window.location.href).searchParams;
    let courseId = params.get(CONSTANT.STAT_COURSE_QUERY_ID);
    let repId = params.get(CONSTANT.STAT_REP_QUERY_ID);
    let contrId = params.get(CONSTANT.STAT_CONTR_QUERY_ID);

    if (contrId != null) {
        // TODO: logic for contr
    } else if (repId != null) {

    } else if (courseId != null) {

    }
}


$(document).ready(function() {

    initDOM();


});