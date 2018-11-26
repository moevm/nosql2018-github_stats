var CONSTANT = {
    URL: 'http://localhost:8090',
};

var SELECTOR = {
    COURSE_NAME: '.jsCourseName',
    REPOSITORY_CONTAINER: '.jsRepositoryContainer',
    REPOSITORY_LINK: '.jsRepositoryLink',
    CURRENT_REPOSITORY_LINK: '.jsCurrentRepositoryLink',
    ADD_REPOSITORY: '.jsAddRepository',
    ADD_COURSE: '.jsAddCourse',
    COURSE_RESULT_CONTAINER: '.jsCourseResultContainer',
    COURSE_INPUT_CONTAINER: '.jsCourseInputContainer',
    RESULT_TABLE: '.jsResultTable',
    TITLE: '.jsTitle'
};

var CLASS = {
    CURRENT_REPOSITORY_LINK: 'jsCurrentRepositoryLink',
};

$(document).ready(function() {

    $(SELECTOR.ADD_REPOSITORY).on('click', function () {
        var $repositoryLink = $(SELECTOR.CURRENT_REPOSITORY_LINK);

        if ($repositoryLink.val()){
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
           repositoryLinkParsed.push({
               repositoryOwner: $(this).val().split("https://github.com/")[1].split("/")[0],
               repositoryName: $(this).val().split("https://github.com/")[1].split("/")[1]
           })
        });

        if ($courseName.val()){
            var data = {};
            data.courseName = $courseName.val();
            data.repositories = repositoryLinkParsed;

            $.ajax({
                url: CONSTANT.URL + "/course/add",
                type: "POST",
                data: JSON.stringify(data),
                contentType: "application/json",
                success: function(result,status,xhr){
                    $(SELECTOR.COURSE_INPUT_CONTAINER).hide();
                    $(SELECTOR.COURSE_RESULT_CONTAINER).show();
                    var resultAppend = "";

                    result.course.repositories.forEach(function (repository) {
                        resultAppend += "<tr><td>";
                        resultAppend += repository.name;
                        resultAppend+= "</td><td>";

                        var countCommits = 0;
                        repository.contributors.forEach(function (contributor) {
                            countCommits += contributor.commits.length;
                        });

                        resultAppend += countCommits;
                        resultAppend += "</td></tr>";
                    });

                    $(SELECTOR.RESULT_TABLE + '> tbody:last-child').append(resultAppend);
                },
                error: function(xhr,status,error){
                }
            });
        }
    })
});