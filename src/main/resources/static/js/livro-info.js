//FUNÇÃO PARA OBTER ID DO LIVRO DA URL
function obterIdLivro() {
  const urlParams = new URLSearchParams(window.location.search);
  return urlParams.get('id');
}


//FUNÇÃO PARA BUSCAR AS INFORMAÇÕES DO LIVRO PELO ID
async function buscarLivroPorId(id) {
      try {
        const response = await fetch(`/exibirDados/${id}`, {
            method: 'GET',
            headers:{
                 'Content-Type': 'application/json'
            },
            credentials: 'include',
        });

        if (!response.ok) {
          throw new Error('Erro ao buscar informações do livro');
        }
        return await response.json();
      } catch (error) {
        console.error('Erro ao buscar informações do livro:', error);
        throw error;
      }
    }


//FUNÇÃO PARA EXIBIR AS INFORMAÇÕES DO LIVRO NA PÁGINA
async function exibirInformacoesLivro() {
  try {
    const id = obterIdLivro();
    if (id) {
      const livro = await buscarLivroPorId(id);
      if (livro) {
        document.getElementById('capaLivro').src = livro.urlCapa;
        document.getElementById('tituloLivro').innerHTML = ` ${livro.titulo}`;
        document.getElementById('autorLivro').innerHTML = ` ${livro.autor}`;
        document.getElementById('paginasLivro').innerHTML = ` ${livro.paginas}`;
        document.getElementById('anoPublicacao').innerHTML = ` ${livro.anoPublicacao}`;
      } else {
        document.getElementById('livro-informacao').innerHTML = '<p>Livro não encontrado.</p>';
      }
    } else {
      document.getElementById('livro-informacao').innerHTML = '<p>ID do livro não encontrado.</p>';
    }
  } catch (error) {
    console.error('Erro ao exibir as informações do livro:', error);
  }
}

// Chama a função para exibir as informações do livro ao carregar a página
window.onload = exibirInformacoesLivro;


// MODAL DE EDIÇÃO
// Função para mostrar o formulário de edição
function mostrarFormularioEdicao() {
  document.getElementById('formulario-edicao').style.display = 'block';
}

// Função para fechar o formulário de edição
function fecharFormularioEdicao() {
  document.getElementById('formulario-edicao').style.display = 'none';
}


//FUNÇÃO PARA ENVIAR A EDIÇÃO DO LIVRO AO SERVIDOR
async function enviarEdicaoLivro() {
  const id = obterIdLivro();
  const paginas = document.getElementById('inputPaginas').value;
  const anoPublicacao = document.getElementById('inputAnoPublicacao').value;
  const dadosAtualizados = {
    paginas: paginas,
    anoPublicacao: anoPublicacao
  };

  try {
    const response = await fetch(`/editarLivro/${id}`, {
      method: 'PUT',
      headers: {
        'Content-Type': 'application/json'
      },
      credentials: 'include',
      body: JSON.stringify(dadosAtualizados)
    });

    if (!response.ok) {
      throw new Error('Erro ao atualizar o livro');
    }

    //ATUALIZA AS INFORMAÇÕES DO LIVRO APÓS A EDIÇÃO
    exibirInformacoesLivro();
    document.getElementById('formulario-edicao').style.display = 'none';
  } catch (error) {
    console.error('Erro ao atualizar o livro:', error);
    alert('Erro ao atualizar o livro.');
  }
}

// FUNÇÃO PARA CANCELAR EDIÇÃO DO LIVRO
function cancelarEdicao() {
  document.getElementById('formulario-edicao').style.display = 'none';
}


// MODAL EXCLUIR
// Função para mostrar o modal de confirmação de exclusão
function mostrarConfirmacaoExclusao() {
  document.getElementById('confirmacaoExclusaoModal').style.display = 'block';
}

// Função para fechar o modal de confirmação de exclusão
function fecharConfirmacaoExclusao() {
  document.getElementById('confirmacaoExclusaoModal').style.display = 'none';
}

// FUNÇÃO PARA EXCLUIR LIVRO APÓS CONFIRMAÇÃO
async function excluirLivroConfirmado() {
  const id = obterIdLivro();

  try {
    const response = await fetch(`/excluirLivro/${id}`, {
      method: 'DELETE',
      headers: {
        'Content-Type': 'application/json'
      },
      credentials: 'include',
    });

    if (!response.ok) {
      throw new Error('Erro ao excluir o livro');
    }

    alert('Livro excluído com sucesso!');
    window.location.href = 'index.html';

  } catch (error) {
    console.error('Erro ao excluir o livro:', error);
    alert('Erro ao excluir o livro.');

  } finally {
    fecharConfirmacaoExclusao();
  }
}