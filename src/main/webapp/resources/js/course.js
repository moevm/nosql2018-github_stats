var CONSTANT = {
    URL: 'http://localhost:8090',
};

var SELECTOR = {
    COURSE_NAME: '.jsCourseName',
    REPOSITORY_CONTAINER: '.jsRepositoryContainer',
    REPOSITORY_NAME: '.jsRepositoryName',
    REPOSITORY_OWNER: '.jsRepositoryOwner',
    CURRENT_REPOSITORY_NAME: '.jsCurrentRepositoryName',
    CURRENT_REPOSITORY_OWNER: '.jsCurrentRepositoryOwner',
    ADD_REPOSITORY: '.jsAddRepository',
    ADD_COURSE: '.jsAddCourse',
    COURSE_RESULT_CONTAINER: '.jsCourseResultContainer',
    COURSE_INPUT_CONTAINER: '.jsCourseInputContainer',
    RESULT_TABLE: '.jsResultTable'
};

var CLASS = {
    CURRENT_REPOSITORY_NAME: '.jsCurrentRepositoryName',
    CURRENT_REPOSITORY_OWNER: '.jsCurrentRepositoryOwner',
};

$(document).ready(function() {
    $(SELECTOR.ADD_REPOSITORY).on('click', function () {
       var $repositoryName = $(SELECTOR.CURRENT_REPOSITORY_NAME),
           $repositoryOwner = $(SELECTOR.CURRENT_REPOSITORY_OWNER);
       if ($repositoryName.val() && $repositoryOwner.val()){
           $(SELECTOR.CURRENT_REPOSITORY_NAME).removeClass(CLASS.CURRENT_REPOSITORY_NAME);
           $(SELECTOR.CURRENT_REPOSITORY_OWNER).removeClass(CLASS.CURRENT_REPOSITORY_OWNER);

           $(SELECTOR.REPOSITORY_CONTAINER).append('<br>');
           $(SELECTOR.REPOSITORY_CONTAINER).append("<input type=\"text\"\n" +
               "               class=\"jsRepositoryName jsCurrentRepositoryName\"\n" +
               "               placeholder=\"Input repository name\">");
           $(SELECTOR.REPOSITORY_CONTAINER).append("<input type=\"text\"\n" +
               "               class=\"jsRepositoryOwner jsCurrentRepositoryOwner\"\n" +
               "               placeholder=\"Input repository owner\">");

       }
    });

    $(SELECTOR.ADD_COURSE).on('click', function () {
        var $courseName = $(SELECTOR.COURSE_NAME),
            $repositoryName = $(SELECTOR.REPOSITORY_NAME),
            $repositoryOwner = $(SELECTOR.REPOSITORY_OWNER);
        var repositoriesIsNotEmpty = function () {
            var flag = true;
            var callback = function () {
                if (!$(this).val()) flag = false;
            };
            $repositoryName.each(callback);
            $repositoryOwner.each(callback)
            return flag;
        }();
        if ($courseName.val() && repositoriesIsNotEmpty){
            var data = {};
            data.courseName = $courseName.val();
            data.repositories = [];
            $repositoryName.each(function (index) {
                data.repositories[index] = {
                    repositoryName: $(this).val()
                };
            });
            $repositoryOwner.each(function (index) {
                data.repositories[index].repositoryOwner = $(this).val();
            });

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