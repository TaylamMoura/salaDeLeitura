<h1>Projeto Sala de Leitura 📚</h1>

>> 🔧 Projeto concluído — próximos passos incluem a migração para uma arquitetura de microsserviços 😃 


## *Visão Geral* 🔍
  -  Esta aplicação foi pensada para registrar o progresso de leitura do usuário e  estimular o foco na leitura. O usuário adiciona livros à "estante virtual", inicia sessões de leitura de um livro específico e utiliza um cronômetro para medir o tempo de leitura. Ao final da leitura, são exibidas estatísticas do tempo e progresso.

## Funcionalidades 🏗️

<h3>Cadastro, Login e Logout de usuários:</h3>

  - Para acessar os recursos, o usuário deve fazer login;
  - Ao acessar pela primeira vez, o usuário deve fazer cadastro.
  - Na página inicial, há o botão 'Sair' para o usuário fazer o logout com segurança.

![login](https://github.com/user-attachments/assets/150bc3aa-94cf-4a12-afd4-4cef3b315e32)

    
<h3>Pesquisa e adição de livros a biblioteca virtual:</h3>

  - Usuário pesquisa o título do livro ou nome do autor através da API do GoogleBook;
  - São retornadas ao usuário as imagens da capas existentes na API;
  - Ao clicar em cima da capa, o livro é adicionado ao banco de dados e sua capa aparece na página inicial da aplicação.

<h3>Edição e exclusão de livros:</h3>

  - Após adicionar um livro, o usuário pode editar as informações do livro ou excluí-lo.
  - Ao clicar sobre a capa do livro, o usuário é redirecionado à página com informações sobre o livro (nome do livro, nome do autor, quantidade de páginas e ano de publicação). O usuário consegue editar a quantidade de páginas e ano de publicação.
  - Também é possível fazer a exclusão do livro ao clicar no botão excluir.

![adicionar-livro](https://github.com/user-attachments/assets/32e23e8b-c768-4a35-a127-884fdb0728e3)


<h3>Iniciar sessão de leitura:</h3>

  - Ao clicar no botão iniciar sessão, o usuário poderá dar *play* no cronômetro e registrar o tempo da sua leitura;
  - Poderá *pausar* a sessão;
  - Ao clicar no botão *stop*, a sessão de leitura é finalizada e o usuário informa em que página do livro ele parou;
  - Com essa informação, a barra de progresso da leitura avança até chegar ao total de páginas do livro.

![sessao-leitura](https://github.com/user-attachments/assets/dbbac71f-a5ec-4388-a3cb-e26511617ae4)


❗ A leitura do livro em si é feita pelo livro físico do usuário leitor ou ebook e não pela aplicação em si. ❗

## Tecnologias utilizadas:  🛠️
<h3>Back-end:</h3>

  - **Java:** o Back-end foi desenvolvido na linguagem Java.
<h3>Front-end:</h3>

  - **HTML:** Linguagem de marcação usada para estruturar conteúdo em páginas web.
  - **CSS:** Estilos que definem a aparência e o layout das páginas web.
  - **JavaScript:** Linguagem de programação que adiciona interatividade e dinamismo às páginas. Responsável pelo Front-end.

<h3>Frameworks:</h3>

  - **Spring Boot:** Framework para simplificar o desenvolvimento de aplicações Java. No projeto foram utilizados:
      >*Spring Security:* Módulo para autenticação e controle de acesso na aplicação;
      
      >*Spring Data JPA:* Abstração para facilitar a interação com bancos de dados usando JPA.
      >
      >*Spring DevTools:* Módulo de recarregamento automático da aplicação, desativação de caches e suporte a Live Reload para visualizar mudanças sem reiniciar manualmente.
  - **Maven:** Ferramenta de automação de compilação e gerenciamento de dependências.

<h3>Banco de Dados:</h3>

  - **MySQL:** Banco de dados relacional utilizado no projeto.
  - **FlyWay:** Ferramenta de migração de banco de dados que permite versionar e automatizar alterações no esquema.

<h3>Autenticação e Segurança:</h3>

  - **BCrypt:** Algoritmo de hash de senhas, projetado para ser resistente a ataques.
  - **JWT (JSON Web Token):** Token compacto usado para autenticação e autorização em APIs.

<h3>Ferramentas de Desenvolvimento:</h3>

  - **Lombok:** Biblioteca que gera automaticamente os getters, setters e construtores.

<h3>Ferramenta de Serialização:</h3>

  - **Gson:** Biblioteca para conversão entre objetos Java e JSON.

## Algumas limitações: ⚠️ 
Durante a integração com a API do Google Books, foram observadas algumas limitações nos dados retornados:

  - Algumas obras não possuem capa visível ou edição específica do título pesquisado.

  - Informações como quantidade de páginas e, principalmente, o ano de publicação, podem vir inconsistentes ou incompletas — normalmente refletindo a edição listada na API, e não a primeira publicação da obra.

  - Essas limitações impactam diretamente a precisão de certas funcionalidades da aplicação, como o progresso de leitura e os metadados exibidos.

Ainda assim, após testar outras opções, a API do Google se mostrou a alternativa mais viável e de integração mais simples, atendendo ao objetivo principal do projeto: permitir a adição rápida e prática de livros à biblioteca virtual.
