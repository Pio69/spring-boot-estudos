# Estudos de Spring Boot

Projeto de estudos para praticar os fundamentos do Spring Boot por meio de uma API simples de produtos.

## Tecnologias

- Java 21
- Spring Boot 4, Spring Web MVC, Spring Data JPA e Bean Validation
- PostgreSQL 17
- Maven
- Docker para executar a aplicação sem instalar Java ou Maven no computador

## Como executar com Docker

Com o Docker Desktop iniciado, crie uma rede para a aplicação e o banco:

```powershell
docker network create estudos-spring
```

Inicie o PostgreSQL:

```powershell
docker run -d --name postgres-estudos --network estudos-spring -e POSTGRES_USER=estudos -e POSTGRES_PASSWORD=senha-estudos -e POSTGRES_DB=estudos -v postgres-estudos-dados:/var/lib/postgresql/data postgres:17
```

Aguarde o banco iniciar (`docker logs postgres-estudos`). Depois, abra o PowerShell na pasta do projeto e execute:

```powershell
docker run --rm -it --name spring-estudos --network estudos-spring -p 8081:8081 -v "${PWD}:/app" -w /app maven:3-eclipse-temurin-21-noble mvn spring-boot:run
```

A API ficará disponível em `http://localhost:8081`. Para encerrá-la, pressione `Ctrl+C` no terminal. Na primeira execução, a imagem e as dependências serão baixadas; como o contêiner é temporário, as dependências também serão baixadas novamente em execuções futuras.

Para parar o banco, execute `docker stop postgres-estudos`; para retomá-lo, use `docker start postgres-estudos`. Os dados persistem no volume `postgres-estudos-dados`. O usuário e a senha acima são apenas para este ambiente local de estudo.

## Endpoints

| Método | Caminho | Descrição |
| --- | --- | --- |
| GET | `/` | Retorna uma mensagem de boas-vindas. |
| GET | `/produto` | Lista os produtos cadastrados. |
| GET | `/produto/{id}` | Busca um produto pelo ID; retorna 404 se não for encontrado. |
| POST | `/produto` | Salva um produto no banco. |
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

Os produtos são armazenados no PostgreSQL usando Spring Data JPA e Hibernate. O projeto usa `spring.jpa.hibernate.ddl-auto=update` para ajustar a estrutura do banco durante os estudos. O `POST` usa o ID informado e pode atualizar um produto já existente com o mesmo ID.
