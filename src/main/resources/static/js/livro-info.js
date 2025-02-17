// Função para obter o ID do livro da URL
function obterIdLivro() {
  const urlParams = new URLSearchParams(window.location.search);
  return urlParams.get('id');
}

// Função para buscar as informações do livro pelo ID
async function buscarLivroPorId(id) {
  try {
    const response = await fetch(`/exibirDados/${id}`);
    if (!response.ok) {
      throw new Error('Erro ao buscar informações do livro');
    }
    return await response.json();
  } catch (error) {
    console.error('Erro ao buscar informações do livro:', error);
    throw error;
  }
}

// Função para exibir as informações do livro na página
async function exibirInformacoesLivro() {
  try {
    const id = obterIdLivro();
    if (id) {
      const livro = await buscarLivroPorId(id);
      if (livro) {
        document.getElementById('capaLivro').src = livro.urlCapa;
        document.getElementById('tituloLivro').textContent = `Livro: ${livro.titulo}`;
        document.getElementById('autorLivro').textContent = `Autor: ${livro.autor}`;
        document.getElementById('paginasLivro').textContent = `Páginas: ${livro.paginas}`;
        document.getElementById('anoPublicacao').textContent = `Ano: ${livro.anoPublicacao}`;
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

// Função para mostrar o formulário de edição
function mostrarFormularioEdicao() {
  document.getElementById('formulario-edicao').style.display = 'block';
}

// Função para fechar o formulário de edição
function fecharFormularioEdicao() {
  document.getElementById('formulario-edicao').style.display = 'none';
}

// Função para enviar a edição do livro ao servidor
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
      body: JSON.stringify(dadosAtualizados)
    });

    if (!response.ok) {
      throw new Error('Erro ao atualizar o livro');
    }

    // Atualize as informações do livro na página após a edição
    exibirInformacoesLivro();
    document.getElementById('formulario-edicao').style.display = 'none';
  } catch (error) {
    console.error('Erro ao atualizar o livro:', error);
    alert('Erro ao atualizar o livro.');
  }
}

// Função para cancelar a edição
function cancelarEdicao() {
  document.getElementById('formulario-edicao').style.display = 'none';
}

// Função para mostrar o modal de confirmação de exclusão
function mostrarConfirmacaoExclusao() {
  document.getElementById('confirmacaoExclusaoModal').style.display = 'block';
}

// Função para fechar o modal de confirmação de exclusão
function fecharConfirmacaoExclusao() {
  document.getElementById('confirmacaoExclusaoModal').style.display = 'none';
}

// Função para excluir o livro após confirmação
async function excluirLivroConfirmado() {
  const id = obterIdLivro();

  try {
    const response = await fetch(`/excluirLivro/${id}`, {
      method: 'DELETE',
      headers: {
        'Content-Type': 'application/json'
      }
    });

    if (!response.ok) {
      throw new Error('Erro ao excluir o livro');
    }

    alert('Livro excluído com sucesso!');
    window.location.href = 'index.html'; // Redireciona para a página inicial após a exclusão
  } catch (error) {
    console.error('Erro ao excluir o livro:', error);
    alert('Erro ao excluir o livro.');
  } finally {
    fecharConfirmacaoExclusao(); // Fecha o modal de confirmação após a tentativa de exclusão
  }
}