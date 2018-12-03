var CLASS = {
    CURRENT_REPOSITORY_LINK: 'jsCurrentRepositoryLink',
};

$(document).ready(function () {

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

        $repositoryLink.each(function () {
            // add filter to empty fields
            repositoryLinkParsed.push({
                repositoryOwner: $(this).val().split("https://github.com/")[1].split("/")[0],
                repositoryName: $(this).val().split("https://github.com/")[1].split("/")[1]
            })
        });

        if ($courseName.val()) {
            var data = {};
            data.courseName = $courseName.val();
            data.repositories = repositoryLinkParsed;
            if (areCredentialsExists()){
                data.credentials = getCredentials();
            }

            $.ajax({
                url: CONSTANT.URL + "/course/add",
                type: "POST",
                data: JSON.stringify(data),
                contentType: "application/json",
                success: function (result, status, xhr) {
                    redirectToCourse(result.course.id)
                },
                error: function (xhr, status, error) {
                }
            });
        }
    });

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