function adicionarLivro(){
    const buscarLivroForm = document.getElementById('buscarLivroForm');
    buscarLivroForm.style.display = 'block';
    buscarLivroForm.innerHTML = `
        <input type="text" id="tituloLivro" placeholder="Digite o título do livro">
        <button type="submit">Buscar</button>
    `;
    buscarLivroForm.onsubmit = buscarLivro;
}

document.addEventListener("DOMContentLoaded", async function() {
    const apiKey = await fetchApiKey();
    fetchLivrosSalvos(apiKey);
});

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

async function buscarLivro(event) {
    event.preventDefault();
    const titulo = document.getElementById('tituloLivro').value;
    const apiKey = await fetchApiKey();

    if(!apiKey){
        console.error('Chave não encontrada');
        return;
    }

    const url = `https://www.googleapis.com/books/v1/volumes?q=${titulo}&key=${apiKey}`;

    try {
         const response = await fetch(url);
         const data = await response.json();
         const livros = data.items;
         const buscarLivroForm = document.getElementById('buscarLivroForm');
         buscarLivroForm.innerHTML = '';

         livros.forEach(livro => {
             const capa = livro.volumeInfo.imageLinks?.thumbnail;
             if (capa) {
                 const img = document.createElement('img');
                 img.src = capa;
                 img.style.cursor = 'pointer';
                 img.onclick = () => adicionarCapa(livro);

                 buscarLivroForm.appendChild(img);
             }
         });

         const voltarBtn = document.createElement('button');
         voltarBtn.textContent = 'Voltar';
         voltarBtn.onclick = () => {
             buscarLivroForm.style.display = 'none';
             buscarLivroForm.innerHTML = `
                 <input type="text" id="tituloLivro" placeholder="Digite o título do livro">
                 <button type="submit">Buscar</button>
             `;
             buscarLivroForm.onsubmit = buscarLivro;
         };
         buscarLivroForm.appendChild(voltarBtn);

    } catch (error) {
         console.error('Erro ao buscar livros:', error);
    }
}

function adicionarCapa(livro) {
    const minhasLeituras = document.getElementById('minhasLeituras');
    const capa = livro.volumeInfo.imageLinks.thumbnail;

    const div = document.createElement('div');
    const img = document.createElement('img');
    img.src = capa;

    div.appendChild(img);
    div.onclick = async () => {

        localStorage.setItem('livroSelecionado', JSON.stringify({
            capa: livro.volumeInfo.imageLinks.thumbnail,
            titulo: livro.volumeInfo.title,
            autor: livro.volumeInfo.authors ? livro.volumeInfo.authors[0] : "N/A",
            paginas: livro.volumeInfo.pageCount
        }));
        // window.location.href = 'meu-livro.html';

        try{
            const response = await fetch('salvarLivro',{
                 method : 'POST',
                 headers: {
                     'Content-Type': 'application/json'
                 },
                 body: JSON.stringify({
                                 titulo: livro.volumeInfo.title,
                                 autor: livro.volumeInfo.authors ? livro.volumeInfo.authors[0] : 'N/A',
                                 paginas: livro.volumeInfo.pageCount,
                                 urlCapa: livro.volumeInfo.imageLinks.thumbnail,
                                 anoPublicacao: livro.volumeInfo.publishedDate ? new Date(livro.volumeInfo.publishedDate).getFullYear() : 'N/A'
                 })
            });

            if(!response.ok){
                throw new Error('Erro ao salvar.');
            }

            const data = await response.text();
            console.log('livro salvo ', data);
            await fetchLivrosSalvos();
            window.location.href = 'meu-livro.html';
        } catch (error){
                console.error('Erro ao salvar Livro: ', error);
        }
    };
    minhasLeituras.appendChild(div);
    document.getElementById('buscarLivroForm').style.display = 'none';
}


document.addEventListener("DOMContentLoaded", function() {
    fetchLivrosSalvos();
});

async function fetchLivrosSalvos(){
    try{
        const response = await fetch('/livrosSalvos');
        const livros = await response.json();
        renderLivros(livros);
    } catch (error){
        console.error('Erro ao buscar livros salvos: ', error);
    }
}

function renderLivros(livros) {
    const container = document.getElementById('minhasLeituras');
    container.innerHTML = '';
    livros.forEach(livro => {
        const livroElement = document.createElement('div');
        livroElement.className = 'livro';

        const capaElement = document.createElement('img');
        capaElement.src = livro.urlCapa;
        capaElement.alt = livro.titulo;

        const tituloElement = document.createElement('p');
        tituloElement.textContent = livro.titulo;

        capaElement.onclick = () => abrirLivro(livro);
        tituloElement.onclick = () => abrirLivro(livro);

        livroElement.appendChild(capaElement);
        livroElement.appendChild(tituloElement);
        container.appendChild(livroElement);
    });
}

function abrirLivro(livro){
    localStorage.setItem('livroSelecionado', JSON.stringify({
            capa: livro.urlCapa,
            titulo: livro.titulo,
            autor: livro.autor,
            paginas: livro.paginas
        }));
        window.location.href = 'meu-livro.html';
}
