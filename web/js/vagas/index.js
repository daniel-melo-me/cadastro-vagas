jQuery(function () {
    $('#data_table_vagas').DataTable();
    listar();
});

function listar() {
    let token = localStorage.getItem('token');

    $.ajax({
        type: "GET",
        url: `${url}/vaga/listar`,
        contentType: "application/json;charset=UTF-8",
        headers: {
            'Accept': 'application/json, text/plain, */*',
            'Content-Type': 'application/json',
            'Authorization': 'Bearer ' + token
        },
        success: function (data) {
            let html = '';
            let botoes = '';
            let perfil = localStorage.getItem('perfil') ?? 'admin';

            if (data.content.length > 0) {
                data.content.forEach(function (item) {

                    if (perfil == 'admin' || perfil == 'professor') {

                        botoes = `
                            <a title="Editar">
                                <button class="btn btn-info" onclick="editar(${item.id, item.titulo, item.descricao, item.institucional, item.dataCriacao, item.salario})">
                                    <em class="fa fa-edit"></em> 
                                </button>
                            </a>
                        `;

                        botoes += `
                            <a title="Suspender">
                                <button class="btn btn-warning" onclick="suspender(${item.id})">
                                    <em class="fa fa-power-off"></em> 
                                </button>
                            </a>
                        `;

                        botoes += `
                            <a title="Excluir">
                                <button class="btn btn-danger" onclick="excluir(${item.id})">
                                    <em class="fa fa-trash"></em> 
                                </button>
                            </a>
                        `;
                    } else if (perfil == 'aluno') {
                        botoes = `
                            <a title="Candidatar"> 
                                <button class="btn btn-success" onclick="excluir(${item.id})">
                                    Candidatar-se
                                </button>
                            </a>
                        `;
                    } else {
                        botoes += '<a class="btn btn-danger">Você não possui permissão</a>';
                    }

                    html += ` <tr class="trCss">`;
                    html += `     <td>${item.titulo}</td>`;
                    html += `     <td>${item.descricao}</td>`;
                    html += `     <td>${item.institucional}</td>`;
                    html += `     <td>${item.dataCriacao}</td>`;
                    html += `     <td>${item.salario ? 'R$' + item.salario : 'A combinar'}</td>`;
                    html += `     <td class="acoes"> ${botoes} </td>`;
                    html += ` </tr>`;

                    /* html += `<tr class="trCss">
                                <td>${item.titulo}</td>
                                <td>${item.descricao}</td>
                                <td>${item.institucional}</td>
                                <td>${item.dataCriacao}</td>
                                <td>${item.salario ? 'R$' + item.salario : 'A combinar'}</td>
                                <td>${botoes}</td>
                            </tr>`; */
                });

                $('#data_table_vagas').DataTable().destroy();
                $('#data_table_vagas tbody').html(html);
                $('#data_table_vagas').DataTable();


                //$('#tBodyIdVagas').html(html);
                //
                //$('#data_table_vagas').DataTable();
            }

        },
        error: function (data) {
            alert('Erro: ' + data.responseJSON.erro);
        }
    });
}