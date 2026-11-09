# Estudos de Spring Boot

Projeto de estudos para praticar os fundamentos do Spring Boot por meio de uma API simples de produtos.

## Tecnologias

- Java 21
- Spring Boot 4, Spring Web MVC e Bean Validation
- Maven
- Docker para executar a aplicação sem instalar Java ou Maven no computador

## Como executar com Docker

Com o Docker Desktop iniciado, abra o PowerShell na pasta do projeto e execute:

```powershell
docker run --rm -it --name spring-estudos -p 8081:8081 -v "${PWD}:/app" -w /app maven:3-eclipse-temurin-21-noble mvn spring-boot:run
```

A API ficará disponível em `http://localhost:8081`. Para encerrá-la, pressione `Ctrl+C` no terminal. Na primeira execução, a imagem e as dependências serão baixadas; como o contêiner é temporário, as dependências também serão baixadas novamente em execuções futuras.

## Endpoints

| Método | Caminho | Descrição |
| --- | --- | --- |
| GET | `/` | Retorna uma mensagem de boas-vindas. |
| GET | `/produto` | Lista os produtos cadastrados. |
| GET | `/produto/{id}` | Busca um produto pelo ID; retorna 404 se não for encontrado. |
| POST | `/produto` | Adiciona um produto à lista. |
| DELETE | `/produto/{id}` | Remove um produto; retorna 204 se encontrado ou 404 caso contrário. |

Exemplo de corpo para `POST /produto`:

```json
{
  "id": 1,
  "nome": "Teclado",
  "preco": 150.0
}
```

O ID deve ser informado, o nome não pode ficar em branco e o preço deve ser maior que zero. Dados inválidos recebem uma resposta `400` com mensagens por campo.

Os produtos são armazenados apenas em memória e são perdidos quando a aplicação é encerrada ou reiniciada. O projeto ainda não utiliza banco de dados.
