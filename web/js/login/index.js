jQuery(function () {
    $("#login").on('click', function () {
        login();
    });

    $(".form-control-input").on('keyup', function(e) {
        if (e.keyCode === 13) {
            login();
        } 
    });
});

function login() {
    let matricula = $("#lemail").val();
    let senha = $("#lpassword").val();

    $.ajax({
        type: "POST",
        url: `${url}/auth/login`,
        contentType: "application/json;charset=UTF-8",
        data: JSON.stringify({matricula: matricula, senha: senha}),
        headers: {
            'Accept': 'application/json, text/plain, */*',
            'Content-Type': 'application/json'
        },
        success: function (data) {
            localStorage.setItem('token', data.token);

            if (data.token) {
                window.location.href = "../vagas/index.html";
            }

            $("#lemail").val('');
            $("#lpassword").val('');
        },
        error: function (data) {
            alert('Erro: ' + data.responseJSON.erro);
            $("#lemail").val('');
            $("#lpassword").val('');
        }
    });
}