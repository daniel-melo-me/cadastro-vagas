jQuery(function () {
    $(".areaAtuacao").chosen(); 
    $('#modalCadastroVaga').modal({backdrop: 'static', keyboard: false});

    $("#btnCadastrarVaga").on('click', function () {
        cadastrarVaga();
    });

    $(".novaVaga").on('click', function () {
        limparCampos();
        carregarTags();
    });
});

function carregarTags() 
{
    let token = localStorage.getItem('token');

    $.ajax({
        type: "GET",
        url: `${url}/tags/listar`,
        contentType: "application/json;charset=UTF-8",
        headers: {
            'Accept': 'application/json, text/plain, */*',
            'Content-Type': 'application/json',
            'Authorization': 'Bearer ' + token
        },
        success: function (data) {
            $.map(data.content, function( tag ) {
                $('#area_atuacao').append(`<option value="${tag.id}">${tag.nome}</option>`);
            });
            $("#area_atuacao").trigger("chosen:updated");
        },
        error: function (data) {
            alert('Erro: ' + data.responseJSON.erro);
        }
    });
}

function cadastrarVaga() 
{
    let titulo = $("#titulo").val();
    let descricao = $("#descricao").val();
    let salario = $("#salario").val();
    let areaAtuacao = $("#area_atuacao").val();
    let institucional = 'INTERNO';
    let token = localStorage.getItem('token');
    let link = $("#link").val() ?? '';
    let tags = '';
    tags += "[";

    for(var i = 0; i<areaAtuacao.length; i++) {
        if(i+1==areaAtuacao.length) {
            tags += JSON.stringify({id: areaAtuacao[i]});
            tags += "]";
        }else {
            tags += JSON.stringify({id: areaAtuacao[i]}) + ", ";
        }
    }

    let newTags = JSON.parse(tags);

    $.ajax({
        type: "POST",
        url: `${url}/vaga/criar`,
        contentType: "application/json;charset=UTF-8",
        data: JSON.stringify({
            "titulo": titulo,
            "descricao": descricao,
            "salario": salario,
            "link": link,
            "tags": newTags,
            "expiracao": "11-11-2023 12:53",
            "institucional": institucional
        }),
        headers: {
            'Accept': 'application/json, text/plain, */*',
            'Content-Type': 'application/json',
            'Authorization': 'Bearer ' + token
        },
        success: function (data) {
            window.location.href = "../vagas/index.html";
        },
        error: function (data) {
            alert('Erro: ' + data.responseJSON.erro);
        }
    });
}

function limparCampos() 
{
    $('#formCadastrarVaga').each (function(){
        this.reset();
    });
}