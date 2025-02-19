// --> TIRAR OS CONSOLE.LOG DEPOIS


// CONFIG APIKEY
async function fetchApiKey() {
    try {
        const response = await fetch('/api-key');
        const data = await response.text();
        return data;
    } catch (error) {
        console.error('Erro ao buscar a chave da API:', error);
        return null;
    }
}

// BUSCA LIVRO NA API
async function buscarLivroPorTitulo(titulo) {
  const apiKey = await fetchApiKey();
  console.log('API Key:', apiKey);

  if (!apiKey) {
     console.error('Chave não encontrada');
     return;
  }
  const url = `https://www.googleapis.com/books/v1/volumes?q=intitle:${titulo}&key=${apiKey}`;
  console.log('URL:', url);

  try {
    const response = await fetch(url);
    console.log('Response:', response);

    if (!response.ok) {
      throw new Error('Erro ao buscar livro');
    }
    const data = await response.json();
    console.log('Data:', data);
    return data.items;
  } catch (error) {
    console.error('Erro ao buscar livro:', error);
    return null;
  }
}

const defaultImageUrl = 'https://via.placeholder.com/150x150';

// Evento onsubmit do formulário
document.getElementById('buscarLivroForm').onsubmit = async function(event) {
  event.preventDefault();
  const titulo = document.getElementById('tituloLivro').value;
  console.log('Título:', titulo); // Verifica o título inserido pelo usuário

  const livros = await buscarLivroPorTitulo(titulo);
  console.log('Livros:', livros); // Verifica os livros retornados pela API
  const resultadoDiv = document.getElementById('resultadoBusca');
  resultadoDiv.innerHTML = ''; // Limpa resultados anteriores

  if (livros) {
    livros.forEach(livro => {
      const tituloLivro = livro.volumeInfo.title;
      const capaLivro = (livro.volumeInfo.imageLinks && livro.volumeInfo.imageLinks.thumbnail) ? livro.volumeInfo.imageLinks.thumbnail : defaultImageUrl;
      console.log('URL da Capa:', capaLivro);

      const livroDiv = document.createElement('div');
      livroDiv.className = 'livro-item';
      livroDiv.innerHTML = `<img src="${capaLivro}" alt="${tituloLivro}"><p>${tituloLivro}</p>`;

      // Adiciona um evento de clique ao livro para salvá-lo no banco de dados
      livroDiv.addEventListener('click', () => {
        const livroData = {
          titulo: livro.volumeInfo.title,
          autor: livro.volumeInfo.authors ? livro.volumeInfo.authors[0] : 'Autor desconhecido',
          paginas: livro.volumeInfo.pageCount || 0,
          urlCapa: capaLivro,
          anoPublicacao: livro.volumeInfo.publishedDate ? livro.volumeInfo.publishedDate.split('-')[0] : new Date().getFullYear()
        };
        console.log('Adicionando livro ao banco de dados:', livroData);
        adicionarLivroAoBanco(livroData);
      });

      console.log('Adicionando livro à div de resultados:', livroDiv);
      resultadoDiv.appendChild(livroDiv);
    });
  } else {
    resultadoDiv.innerHTML = '<p>Livro não encontrado.</p>';
  }

  console.log('Exibindo modal');
  document.getElementById('resultadoModal').style.display = 'block'; // Exibe o modal
};


// Função para adicionar o livro na página
function adicionarLivroNaPagina(livro) {
    const minhasLeiturasDiv = document.getElementById('minhasLeituras');
    const tituloLivro = livro.titulo;
    const capaLivro = livro.urlCapa ? livro.urlCapa : defaultImageUrl;

    const livroDiv = document.createElement('div');
    livroDiv.classList.add('livro-item'); // Adiciona uma classe para o estilo

    // Cria o elemento <a>
    const linkElement = document.createElement('a');
    linkElement.href = `meu-livro.html?id=${livro.id}`; // Redireciona para meu-livro.html com o ID do livro

    const imgElement = document.createElement('img');
    imgElement.src = capaLivro;
    imgElement.alt = tituloLivro;

    const tituloElement = document.createElement('p');
    tituloElement.textContent = tituloLivro;

    // Adiciona a imagem e o título ao link
    linkElement.appendChild(imgElement);
    linkElement.appendChild(tituloElement);

    // Adiciona o link ao div do livro
    livroDiv.appendChild(linkElement);

    minhasLeiturasDiv.appendChild(livroDiv);
}

// Adiciona um evento de clique para fechar o modal
document.querySelector('.close').onclick = function() {
    document.getElementById('resultadoModal').style.display = 'none';
};

// Fecha o modal quando o usuário clica fora da janela de conteúdo
window.onclick = function(event) {
    if (event.target == document.getElementById('resultadoModal')) {
        document.getElementById('resultadoModal').style.display = 'none';
    }
};

// Função para adicionar livro ao banco de dados
async function adicionarLivroAoBanco(livro) {
  const livroData = {
    titulo: livro.titulo,
    autor: livro.autor || 'Autor desconhecido',
    paginas: livro.paginas || 0,
    urlCapa: livro.urlCapa || 'https://via.placeholder.com/150',
    anoPublicacao: livro.anoPublicacao || new Date().getFullYear()
  };

  try {
    const response = await fetch('/salvarLivro', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify(livroData)
    });
    if (!response.ok) {
      throw new Error('Erro ao adicionar livro ao banco de dados');
    }
    const novoLivro = await response.json();
    adicionarLivroNaPagina(novoLivro);
    // Fecha a janela modal
    document.getElementById('resultadoModal').style.display = 'none';
  } catch (error) {
    console.error('Erro ao adicionar livro ao banco de dados:', error);
  }
}

// Função para exibir os livros salvos na página
async function ExibirLivrosNaPag() {
  try {
    const response = await fetch('/livrosSalvos');
    if (!response.ok) {
      throw new Error('Erro ao buscar livros do banco de dados');
    }
    const livros = await response.json();
    const minhasLeiturasDiv = document.getElementById('minhasLeituras');
    minhasLeiturasDiv.innerHTML = ''; // Limpa os livros anteriores
    /*
    livros.forEach(livro => {
      const tituloLivro = livro.titulo;
      const capaLivro = livro.urlCapa ? livro.urlCapa : defaultImageUrl;
      const livroDiv = document.createElement('div');
      livroDiv.innerHTML = `<img src="${capaLivro}" alt="${tituloLivro}"><p>${tituloLivro}</p>`;
      minhasLeiturasDiv.appendChild(livroDiv);
    });*/
    livros.forEach(livro => {
    adicionarLivroNaPagina(livro);
      });
  } catch (error) {
    console.error('Erro ao carregar minhas leituras:', error);
  }
}
// Chama a função ExibirLivrosNaPag ao carregar a página
window.onload = ExibirLivrosNaPag;
