$(document).ready(function () {
    let courseId = getCurrentCourseId();
    let repId = getCurrentRepId();
    let contrId = getCurrentContrId();

    $(SELECTOR.DROPDOWN_TYPE).toggle(courseId !== null);

    setupCourseDropdown(courseId, repId, contrId);
    setupRepDropdown(courseId, repId, contrId);
    setupContrDropdown(courseId, repId, contrId);
});

function setTitle(title) {
    $(SELECTOR.TITLE).append(title)
}
function setupCourseDropdown(courseId, repId, contrId) {
    $.ajax({
        url: CONSTANT.URL + "/course/getCourseNames",
        type: "GET",
        success: function (result, status, xhr) {
            result.courseNames.forEach(function (pair) {
                $(SELECTOR.DROPDOWN_COURSE).append(`<option value="${pair.id}">${pair.name}</option>`);

                if (courseId !== null && repId == null && contrId == null && courseId === pair.id) {
                    setTitle(pair.name)
                }
            });
            if (courseId != null) {
                $(SELECTOR.DROPDOWN_COURSE).val(courseId)
            }


        },
        error: function (xhr, status, error) {
        }
    });

    $(SELECTOR.DROPDOWN_COURSE).change(function () {
        let courseId = $(SELECTOR.DROPDOWN_COURSE).val();
        if (courseId !== DROPDOWN_CONSTANT.DEFAULT) {
            redirectToCourse(courseId)
        }
    });
}

function setupRepDropdown(courseId, repId, contrId) {
    $(SELECTOR.DROPDOWN_REP).toggle(courseId !== null);

    if (courseId !== null) {
        var data = {};
        data.courseId = courseId;
        $.ajax({
            url: CONSTANT.URL + "/course/getRepositoryNames",
            type: "POST",
            data: JSON.stringify(data),
            contentType: "application/json",
            success: function (result, status, xhr) {
                result.repositoryNames.forEach(function (pair) {
                    $(SELECTOR.DROPDOWN_REP).append(`<option value="${pair.id}">${pair.name}</option>`)

                    if (repId != null && contrId == null && repId === pair.id) {
                        setTitle(pair.name)
                    }
                });
                if (repId != null) {
                    $(SELECTOR.DROPDOWN_REP).val(repId)
                }
            },
            error: function (xhr, status, error) {
            }
        });

        $(SELECTOR.DROPDOWN_REP).change(function () {
            let repId = $(SELECTOR.DROPDOWN_REP).val();
            if (repId !== DROPDOWN_CONSTANT.DEFAULT) {
                redirectToRep(repId)
            }
        });
    }
}

function setupContrDropdown(courseId, repId, contrId) {
    $(SELECTOR.DROPDOWN_CONTR).toggle(repId !== null && courseId !== null);

    if (courseId !== null && repId !== null) {
        var data = {};
        data.courseId = courseId;
        data.repositoryId = repId;
        $.ajax({
            url: CONSTANT.URL + "/repository/getContributorNames",
            type: "POST",
            data: JSON.stringify(data),
            contentType: "application/json",
            success: function (result, status, xhr) {
                result.contributorNames.forEach(function (contributorName) {
                    $(SELECTOR.DROPDOWN_CONTR).append(`<option value="${contributorName}">${contributorName}</option>`);

                    if (contrId != null && contrId === contributorName) {
                        setTitle(contributorName)
                    }
                });
                if (contrId != null) {
                    $(SELECTOR.DROPDOWN_CONTR).val(contrId)
                }
            },
            error: function (xhr, status, error) {
            }
        });

        $(SELECTOR.DROPDOWN_CONTR).change(function () {
            let repId = $(SELECTOR.DROPDOWN_CONTR).val();
            if (repId !== DROPDOWN_CONSTANT.DEFAULT) {
                redirectToContr(repId)
            }
        });
    }
}