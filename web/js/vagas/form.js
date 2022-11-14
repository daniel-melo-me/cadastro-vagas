jQuery(function () {
    $(".areaAtuacao").chosen();
    $('#modalCadastroVaga').modal({ backdrop: 'static', keyboard: false });

    $("#btnCadastrarVaga").on('click', function () {
        cadastrarVaga();
    });

    $(".novaVaga").on('click', function () {
        limparCampos();
        carregarTags();
    });
});

function carregarTags() {
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
            $.map(data.content, function (tag) {
                $('#area_atuacao').append(`<option value="${tag.id}">${tag.nome}</option>`);
            });
            $("#area_atuacao").trigger("chosen:updated");
        },
        error: function (data) {
            toastr.error('Erro: ' + data.responseJSON.erro);
        }
    });
}

function cadastrarVaga() {
    let titulo = $("#titulo").val();
    let descricao = $("#descricao").val();
    let salario = $("#salario").val();
    let areaAtuacao = $("#area_atuacao").val();
    let institucional = 'INTERNO';
    let token = localStorage.getItem('token');
    let link = $("#link").val() ?? '';
    let tags = '';
    tags += "[";

    //$(".spinner-wrapper").show();

    for (let i = 0; i < areaAtuacao.length; i++) {
        if (i + 1 == areaAtuacao.length) {
            tags += JSON.stringify({ id: areaAtuacao[i] });
            tags += "]";
        } else {
            tags += JSON.stringify({ id: areaAtuacao[i] }) + ", ";
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
            $('#modalCadastroVaga').modal('hide');
            $('#data_table_vagas').DataTable();
            listar();
        },
        error: function (data) {
            alert('Error: ' + data.responseJSON.erro);
        },
        always: function () {
            // alert('Fucnioou');
            // $(".spinner-wrapper").hide();
        }
    });
}

function excluir(id) {
    let token = localStorage.getItem('token');

    // Falta verificar o motivo de n poder excluir
    // tb o X do modal confirm estar alinhado a esquerda
    appUtil.confirmBox('Deseja realmente excluir essa vaga???', function (retorno) {
        if (retorno) {
            appUtil.showLoader();
            $.ajax({
                type: "DELETE",
                url: `${url}/vaga/deletar/${id}`,
                headers: {
                    'Authorization': 'Bearer ' + token
                },
                success: function (data) {
                    console.log(data);
                    toastr.success("Registro excluído com sucesso!");
                    listar();
                },
                error: function (data) {
                    // appUtil.createFlashMesseger('Deu ruim', 401, '#flashMensager');
                    console.log(data);
                    toastr.options.closeButton = true;
                    toastr.options.closeMethod = 'fadeOut';
                    toastr.options.preventDuplicates = true;
                    toastr.options.progressBar = true;
                    toastr.error("Erro ao tentar excluir o registro.", 'Error', { timeOut: 5000 });
                    //alert('Error: ' + data.responseJSON.erro);
                },
                always: function () {
                    appUtil.hideLoader();
                }
            });

        }
    });
}

function editar(id, titulo, descricao, institucional, dataCriacao, salario, link, tags) {
    console.log('id ' + id, 'titulo ' + titulo, 'descricao ' + descricao, 'institucional ' + institucional, 'dataCriacao ' + dataCriacao, 'salario ' + salario, 'link ' + link, 'tags ' + tags);

    return false;

    limparCampos();
    $("#titulo").val(titulo);
    $("#descricao").val(descricao);
    $("#salario").val(salario);
    $("#link").val(link);
    $("#id").val(id);
    $("#institucional").val(institucional);
    $("#dataCriacao").val(dataCriacao);

    carregarTags();

    for (let i = 0; i < tags.length; i++) {
        $("#area_atuacao").val(tags[i].id);
        $("#area_atuacao").trigger("chosen:updated");
    }

    $('#modalCadastroVaga').modal('show');

    $("#btnCadastrarVaga").on('click', function () {
        atualizarVaga();
    });



}

function atualizarVaga(data) {
    let token = localStorage.getItem('token');

    $.ajax({
        type: "GET",
        url: `${url}/vaga/editar/${id}`,
        headers: {
            'Accept': 'application/json, text/plain, */*',
            'Content-Type': 'application/json',
            'Authorization': 'Bearer ' + token
        },
        success: function (data) {
            console.log(data);
            toastr.success("Registro atualizado com sucesso!");
            listar();
        },
        error: function (data) {
            toastr.error('Erro: ' + data.responseJSON.erro);
        }
    });
}

function limparCampos() {
    $('#formCadastrarVaga').each(function () {
        this.reset();
    });
}