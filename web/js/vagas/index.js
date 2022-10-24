
jQuery(function () {
    listar();
});

function listar() 
{
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
            console.log('aqui', data.content.length, data);
            let html = '';
            let botoes = '';
            let perfil = localStorage.getItem('perfil') ?? 'admin';

            if (data.content.length > 0) {
                data.content.forEach(function (item) {
                    html += `<tr class="trCss">
                                <td>${item.titulo}</td>
                                <td>${item.descricao}</td>
                                <td>${item.institucional}</td>
                                <td>${item.dataCriacao}</td>
                                <td>${item.salario}</td>
                                <td>
                                    <a title="Editar">
                                        <button class="btn btn-info" onclick="editar(${item.id})">
                                            <em class="fa fa-edit"></em> 
                                        </button>
                                    </a>
                                    <a title="Suspender">
                                        <button class="btn btn-warning" onclick="suspender(${item.id})">
                                            <em class="fa fa-power-off"></em> 
                                        </button>
                                    </a>
                                    <a title="Excluir">
                                        <button class="btn btn-danger" onclick="excluir(${item.id})">
                                            <em class="fa fa-trash"></em> 
                                        </button>
                                    </a>
                                </td>
                            </tr>`;
                });
                $('#tBodyIdVagas').html(html);
            }
            
        },
        error: function (data) {
            alert('Erro: ' + data.responseJSON.erro);
        }
    });
}