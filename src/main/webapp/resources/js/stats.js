$(document).ready(function () {
    setupLeftMenu();
    setupDeleteButton();
    loadInfoAndDrawCharts();
});

function loadInfoAndDrawCharts() {
    let courseId = getCurrentCourseId();
    let repId = getCurrentRepId();
    let contrId = getCurrentContrId();
    let itemType = getCurrentItemType();

    if (courseId != null && repId != null && contrId != null && itemType != null) {
        loadContrInfoAndShow(courseId, repId, contrId, itemType)
    } else if (courseId != null && repId != null && itemType != null) {
        loadRepInfoAndShow(courseId, repId, itemType)
    } else if (courseId != null && itemType != null) {
        loadCourseInfoAndShow(courseId, itemType)
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
            redirectToCourse(getCurrentCourseId())
        },
        error: function (xhr, status, error) {
            onError(xhr.responseJSON.error);
        }
    });
}

function deleteCurrentCourse() {
    let data = {};
    data.courseId = getCurrentCourseId();
    $.ajax({
        url: CONSTANT.URL + "/course/deleteCourse",
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

function loadCourseInfoAndShow(courseId, itemType) {
    var data = {};
    data.courseId = courseId;
    data.itemType = itemType;

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
            onError(xhr.responseJSON.error);
        }
    });
}

function loadRepInfoAndShow(courseId, repId, itemType) {
    var data = {};
    data.courseId = courseId;
    data.repositoryId = repId;
    data.itemType = itemType;

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
            onError(xhr.responseJSON.error);
        }
    });
}

function loadContrInfoAndShow(courseId, repId, contrId, itemType) {
    var data = {};
    data.courseId = courseId;
    data.repositoryId = repId;
    data.contributorName = contrId;
    data.itemType = itemType;

    $.ajax({
        url: CONSTANT.URL + "/contributor/getItems",
        type: "POST",
        data: JSON.stringify(data),
        contentType: "application/json",
        success: function (result, status, xhr) {
            drawTimeline({dataset: result.result.timeline.dataset, labels: result.result.timeline.labels});
        },
        error: function (xhr, status, error) {
            onError(xhr.responseJSON.error);
        }
    });
}