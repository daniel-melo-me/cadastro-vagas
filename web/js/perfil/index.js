jQuery(function () {
  /*$(".perfilMenu").on("click", function () {
    alert("bem vindo");
    carregarDados();
  });*/
  carregarDados();
  $("#curriculo").on('click', function () {
    cadastrarCurriculo();
  });
});

getUsuario = () => {
  $.ajax({
    type: "GET",
    url: `${url}/usuario/getUsuario`,
    headers: {
      'Accept': 'application/json, text/plain, */*',
      'Content-Type': 'application/json',
      'Authorization': 'Bearer ' + token
    },
    success: function (data) {
        console.log("data.id " + data.id);
        return data.id;
    },
    error: function (data) {
      alert('Erro: ' + data.responseJSON.erro);
    },
    always: function () {
      appUtil.hideLoader();
    }
    
  })
}

function carregarDados() {
  let token = localStorage.getItem('token');
  
  $.ajax({
    type: "GET",
    url: `${url}/usuario/getUsuario`,
    headers: {
      'Accept': 'application/json, text/plain, */*',
      'Content-Type': 'application/json',
      'Authorization': 'Bearer ' + token
    },
    success: function (data) {
    console.log(data);

    let perfil = "";

    switch (data.perfis.nome) {
        case "ROLE_ADMIN": 
           perfil = "Administrador";
           break;
        case "ROLE_ALUNO":
          perfil = "Aluno";
          break;
        case "ROLE_PROFESSOR":
          perfil = "Professor";
          break;
        default:
          perfil = "Nenhum perfil atribuido";    
    }

    $("#nomePerfil").html(data.nome); 
    $("#dataCriacao").html(data.dataCriacao);
    $("#matricula").html(data.matricula);
    $("#emailPerfil").html(data.email);
    $("#perfil").html(perfil);


    

          /* let html = '';
          console.log("data " + data)
          html += `<p>${item.matricula}</p>`  
          html += `<p>${item.nome}</p>`  
          html += `<p>${item.email}</p>`
          html += `<p>${item.perfil}</p>` */
      
    },
    error: function (data) {
      alert('Erro: ' + data.responseJSON.erro);
    },
    always: function () {
      appUtil.hideLoader();
    }
    
  })
  
}

function cadastrarCurriculo() {
  let titulo = $("#titulo").val();
  let descricao = $("#descricao").val();
  appUtil.showLoader('Cadastrando...');

  $.ajax({
      type: "POST",
      url: `${url}/curriculo/criar`,
      contentType: "application/json;charset=UTF-8",
      data: JSON.stringify({
          "titulo": titulo,
          "descricao": descricao
      }),
      headers: {
          'Accept': 'application/json, text/plain, */*',
          'Content-Type': 'application/json',
          'Authorization': 'Bearer ' + token
      },
      success: function (data) {
          carregarDados()
          toastr.options.closeButton = true;
          toastr.options.closeMethod = 'fadeOut';
          toastr.options.preventDuplicates = true;
          toastr.options.progressBar = true;
          toastr.success("Currículo cadastrado com sucesso!", 'Sucesso', { timeOut: 5000 });
      },
      error: function (data) {
          toastr.options.closeButton = true;
          toastr.options.closeMethod = 'fadeOut';
          toastr.options.preventDuplicates = true;
          toastr.options.progressBar = true;
          toastr.error(data.responseJSON.erro, 'Error', { timeOut: 5000 });
      },
      always: function () {
          appUtil.hideLoader();
      }
  });
}