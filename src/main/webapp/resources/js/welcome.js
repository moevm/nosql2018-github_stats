var CLASS = {
    CURRENT_REPOSITORY_LINK: 'jsCurrentRepositoryLink',
};

$(document).ready(function () {

    $(SELECTOR.BACKUP).on('click', function () {
        getDump()
    });

    $(SELECTOR.BACKUP_TO_SERVER).on('click', function () {
        loadDumpToServer()
    });

    $(SELECTOR.ADD_REPOSITORY).on('click', function () {
        var $repositoryLink = $(SELECTOR.CURRENT_REPOSITORY_LINK);

        if ($repositoryLink.val()) {
            $(SELECTOR.CURRENT_REPOSITORY_LINK).removeClass(CLASS.CURRENT_REPOSITORY_LINK);

            $(SELECTOR.REPOSITORY_CONTAINER).append('<br>');
            $(SELECTOR.REPOSITORY_CONTAINER).append("<input type=\"text\"\n" +
                "               class=\"jsRepositoryLink jsCurrentRepositoryLink center\"\n" +
                "               placeholder=\"Input repository link\">");

        }
    });

    $(SELECTOR.ADD_COURSE).on('click', function () {
        var $courseName = $(SELECTOR.COURSE_NAME),
            $repositoryLink = $(SELECTOR.REPOSITORY_LINK),
            repositoryLinkParsed = [];

        if ($courseName.val()) {
            try {
                $repositoryLink.each(function () {
                    // add filter to empty fields
                    if (!$(this).val().match(/^https:\/\/github.com\/[a-zA-Z0-9_\-]+\/[a-zA-Z0-9_\-]+$/)){
                        throw new SyntaxError("Invalid link to git repository");
                    }
                    repositoryLinkParsed.push({
                        repositoryOwner: $(this).val().split("https://github.com/")[1].split("/")[0],
                        repositoryName: $(this).val().split("https://github.com/")[1].split("/")[1]
                    })

                });
            } catch (e) {
                onError(e.message);
                return;
            }

            var data = {};
            data.courseName = $courseName.val();
            data.repositories = repositoryLinkParsed;

            $.ajax({
                url: CONSTANT.URL + "/course/add",
                type: "POST",
                data: JSON.stringify(data),
                contentType: "application/json",
                success: function (result, status, xhr) {
                    redirectToCourse(result.course.id)
                },
                error: function (xhr, status, error) {
                    onError(xhr.responseJSON.error);
                }
            });
        } else {
            onError("Input course name !");
        }
    });
});