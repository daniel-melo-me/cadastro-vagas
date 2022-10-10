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
    let areaAtuacao = $("#area_atuacao").val();
    let institucional = 'INTERNO';
    let token = localStorage.getItem('token');
    let link = 'https://www.turing.com/pt/';
    let tags = '';

    areaAtuacao.forEach(element => {
        tags += JSON.stringify({id: element});
    });

    // Ta retornando {"id":"1"}{"id":"2"}{"id":"3"}{"id":"4"}{"id":"5"}
    // tem que transformar nisso
    /* tags:[
        {"id": "1"},
        {"id": "2"},
        {"id": "3"},
        {"id": "4"},
        {"id": "5"}
    ] */

    console.log(tags);

    $.ajax({
        type: "POST",
        url: `${url}/vaga/criar`,
        contentType: "application/json;charset=UTF-8",
        data: JSON.stringify({
            "titulo": titulo,
            "descricao": descricao,
            "link": link,
            "tags":[
                JSON.stringify({id: 1})
            ],
            "expiracao": "11-11-2023 12:53",
            "areaAtuacao": areaAtuacao, 
            institucional: institucional
        }),
        headers: {
            'Accept': 'application/json, text/plain, */*',
            'Content-Type': 'application/json',
            'Authorization': 'Bearer ' + token
        },
        success: function (data) {
            alert('Vaga cadastrada com sucesso!');
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