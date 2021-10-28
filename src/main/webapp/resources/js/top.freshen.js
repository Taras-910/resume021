var freshenUrl = "profile/freshen";
let spinner1 = document.getElementById("spinner1");
let spinner2 = document.getElementById("spinner2");
let count = document.getElementById("count");
let count1 = document.getElementById("count1");

function refreshDB() {
    // count.style.visibility = 'hidden';
    // count1.style.visibility = 'hidden';
    spinner1.style.visibility = 'hidden';
    spinner2.style.visibility = 'hidden';
    $('#detailsRefreshForm').find(":input").val("");
    $("#refreshRow").modal();
}

function sendRefresh() {
    count.style.visibility = 'visible';
    spinner1.style.visibility = 'visible';
    spinner2.style.visibility = 'visible';
    let languageFilter = document.getElementById('language');
    let levelFilter = document.getElementById('level');
    let workplaceFilter = document.getElementById('workplace');
    languageFilter.value = document.getElementById('languageTask').value;
    levelFilter.value = document.getElementById('levelTask').value;
    workplaceFilter.value = document.getElementById('workplaceTask').value;
    $.ajax({
        type: "POST",
        url: freshenUrl,
        data: $("#detailsRefreshForm").serialize()
    }).done(function () {
        $("#refreshRow").modal("hide");
        count1.style.visibility = 'visible';
        updateCount();
        ctx.updateTable();
        successNoty("Update has finished ");
    });
}

function updateCount() {
        $.ajax({
            type: "GET",
            url: "profile/resumes/count"
        }).done(function (count) {
            document.getElementById('count').value = count;
            $.get("profile/resumes/last", function (last) {
                successNoty("There are loaded " + last + " resumes today");
            });
        });
}
