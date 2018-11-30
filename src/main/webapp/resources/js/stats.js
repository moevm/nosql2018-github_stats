$(document).ready(function () {
    setupLeftMenu();
    setupDeleteButton();
    loadInfoAndDrawCharts();
});

function loadInfoAndDrawCharts() {
    let courseId = getCurrentCourseId();
    let repId = getCurrentRepId();
    let contrId = getCurrentContrId();

    if (courseId != null && repId != null && contrId != null) {
        loadContrInfoAndShow(courseId, repId, contrId)
    } else if (courseId != null && repId != null) {
        loadRepInfoAndShow(courseId, repId)
    } else if (courseId != null) {
        loadCourseInfoAndShow(courseId)
    }
}

function setupLeftMenu() {
    $(SELECTOR.LEFT_MENU).toggle(getCurrentContrId() == null);
    $(SELECTOR.LEFT_MENU_ITEM).on("click", function () {
        $(SELECTOR.LEFT_MENU_ITEM_CLICKED).removeClass(CLASS_WITHOUT_DOT.LEFT_MENU_ITEM_CLICKED);
        $(this).addClass(CLASS_WITHOUT_DOT.LEFT_MENU_ITEM_CLICKED);

        $(SELECTOR.CANVAS_TIMELINE).toggle($(this).hasClass(CLASS_WITHOUT_DOT.LEFT_MENU_ITEM_CHART_COMMON));
        $(SELECTOR.CANVAS_TOP).toggle($(this).hasClass(CLASS_WITHOUT_DOT.LEFT_MENU_ITEM_CHART_TOP));
        $(SELECTOR.CANVAS_CIRCLE).toggle($(this).hasClass(CLASS_WITHOUT_DOT.LEFT_MENU_ITEM_CHART_CONTR_CIRCLE));
    });
    $(SELECTOR.LEFT_MENU_ITEM_CLICKED).trigger("click");
}

function setupDeleteButton() {
    $(SELECTOR.DELETE_BUTTON).toggle(getCurrentContrId() == null);
    if (getCurrentContrId() == null) {
        let typeName = getCurrentRepId() != null ? "repository" : "course";
        $(SELECTOR.DELETE_BUTTON).append(`Delete current ${typeName}`);
        $(SELECTOR.DELETE_BUTTON).click(function () {
            if (getCurrentRepId() != null) {
                deleteCurrentRep()
            } else if (getCurrentCourseId() != null) {
                deleteCurrentCourse()
            }
        })
    }
}

function deleteCurrentRep() {
    let data = {};
    data.courseId = getCurrentCourseId();
    data.repositoryId = getCurrentRepId();
    $.ajax({
        url: CONSTANT.URL + "/course/deleteRepository",
        type: "DELETE",
        data: JSON.stringify(data),
        contentType: "application/json",
        success: function (result, status, xhr) {
            redirectToCourse(getCurrentRepId())
        },
        error: function (xhr, status, error) {
        }
    });
}

function deleteCurrentCourse() {
    let data = {};
    data.courseId = getCurrentCourseId();
    data.repositoryId = getCurrentRepId();
    $.ajax({
        url: CONSTANT.URL + "/course/deteleCourse",
        type: "DELETE",
        data: JSON.stringify(data),
        contentType: "application/json",
        success: function (result, status, xhr) {
            redirectToMain()
        },
        error: function (xhr, status, error) {
        }
    });
}

function loadCourseInfoAndShow(courseId) {
    var data = {};
    data.courseId = courseId;

    $.ajax({
        url: CONSTANT.URL + "/course/get",
        type: "POST",
        data: JSON.stringify(data),
        contentType: "application/json",
        success: function (result, status, xhr) {
            drawCircleDiagram({dataset: result.result.circle.dataset, labels: result.result.circle.labels});
            drawTopDiagram({dataset: result.result.top.dataset, labels: result.result.top.labels});
            drawTimeline({dataset: result.result.timeline.dataset, labels: result.result.timeline.labels});
        },
        error: function (xhr, status, error) {
        }
    });
}

function loadRepInfoAndShow(courseId, repId) {
    var data = {};
    data.courseId = courseId;
    data.repositoryId = repId;

    $.ajax({
        url: CONSTANT.URL + "/repository/getContributors",
        type: "POST",
        data: JSON.stringify(data),
        contentType: "application/json",
        success: function (result, status, xhr) {
            drawCircleDiagram({dataset: result.result.circle.dataset, labels: result.result.circle.labels});
            drawTopDiagram({dataset: result.result.top.dataset, labels: result.result.top.labels});
            drawTimeline({dataset: result.result.timeline.dataset, labels: result.result.timeline.labels});

        },
        error: function (xhr, status, error) {
        }
    });
}

function loadContrInfoAndShow(courseId, repId, contrId) {
    var data = {};
    data.courseId = courseId;
    data.repositoryId = repId;
    data.contributorName = contrId;

    $.ajax({
        url: CONSTANT.URL + "/contributor/getCommits",
        type: "POST",
        data: JSON.stringify(data),
        contentType: "application/json",
        success: function (result, status, xhr) {

        },
        error: function (xhr, status, error) {
        }
    });
}