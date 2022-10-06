jQuery(function () {
    $("#login").click(function () {
        login();
    });
    
});


function login() {
    let matricula = document.getElementById("lemail").value;
    let senha = document.getElementById("lpassword").value;

    let mat = $("#lemail").val();
    let pass = $("#lpassword").val();

    console.log('Mat: '+matricula, 'Pass: '+senha, 'Mat2: '+mat, 'Pass2: '+pass);

    $.ajax({
        type: "POST",
        url: "http://localhost:8080/auth/login",
        contentType: "application/json;charset=UTF-8",
        data: JSON.stringify({matricula: mat, senha: pass}),
        headers: {
            'Accept': 'application/json, text/plain, */*',
            'Content-Type': 'application/json'
        },
        success: function (data) {
            console.log('Deu sucesso',data);
            if (data.code == 0) {
                window.location.href = "/index";
            } else {
                alert(data.msg);
            }
        },
        error: function (data) {
            console.log('Deu erro',data);
            alert(data.msg);
        }
    });
}