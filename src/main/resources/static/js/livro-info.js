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
