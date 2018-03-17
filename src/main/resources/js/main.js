(function main() {
    $("#uploadBtn").click(function () {
        var data = new FormData();
        data.append('uploaded_file', $("#pic")[0].files[0]);

        $.ajax({
            url: "/api/parse",
            data: data,
            method: "POST",
            processData: false,
            contentType: false,
            success: function(data) {
                $("#exp-input").val(data);
            }
        });
    });

    $("#computeBtn").click(function () {
        var expression = $("#exp-input").val();
        var data = {
            "exp": expression
        };

        $.ajax({
            url: "/api/compute",
            data: data,
            method: "POST",
            success: function (data) {
                $("#res-output").val(data);
            }
        });
    });
})();
