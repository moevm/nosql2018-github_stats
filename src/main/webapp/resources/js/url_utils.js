function getCurrentCourseId() {
    return new URLSearchParams(window.location.search).get(CONSTANT.STAT_COURSE_QUERY_ID)
}

function getCurrentRepId() {
    return new URLSearchParams(window.location.search).get(CONSTANT.STAT_REP_QUERY_ID)
}

function getCurrentContrId() {
    return new URLSearchParams(window.location.search).get(CONSTANT.STAT_CONTR_QUERY_ID)
}

function getCurrentItemType() {
    return new URLSearchParams(window.location.search).get(CONSTANT.STAT_TYPE_QUERY_ID)
}

function redirectToCourse(courseId) {
    let searchParams = new URLSearchParams();
    searchParams.set(CONSTANT.STAT_COURSE_QUERY_ID, courseId);
    searchParams.set(CONSTANT.STAT_TYPE_QUERY_ID, DROPDOWN_CONSTANT.COMMIT);
    window.location.href = `${CONSTANT.STAT_URL}?${searchParams.toString()}`;
}

function redirectToRep(repId) {
    let searchParams = new URLSearchParams(window.location.search);
    searchParams.set(CONSTANT.STAT_REP_QUERY_ID, repId);
    searchParams.delete(CONSTANT.STAT_CONTR_QUERY_ID);
    window.location.href = `${CONSTANT.STAT_URL}?${searchParams.toString()}`;
}

function redirectToContr(contrId) {
    let searchParams = new URLSearchParams(window.location.search);
    searchParams.set(CONSTANT.STAT_CONTR_QUERY_ID, contrId);
    window.location.href = `${CONSTANT.STAT_URL}?${searchParams.toString()}`;
}

function redirectToMain() {
    window.location.href = CONSTANT.URL;
}

function changeItemType(newItemType) {
    let searchParams = new URLSearchParams(window.location.search);
    searchParams.set(CONSTANT.STAT_TYPE_QUERY_ID, newItemType);
    window.location.href = `${CONSTANT.STAT_URL}?${searchParams.toString()}`;
}