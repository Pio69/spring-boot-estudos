# Estudos de Spring Boot

API de produtos e categorias criada para praticar Spring Boot, persistência, testes e segurança.

## Tecnologias

- Java 21, Spring Boot 4, Spring Web MVC, Spring Data JPA e Bean Validation
- Spring Security e Spring Boot Actuator
- PostgreSQL 17, Maven e Docker

## Executar com Docker

Com o Docker Desktop iniciado, crie a rede e o banco uma vez:

```powershell
docker network create estudos-spring
docker run -d --name postgres-estudos --network estudos-spring -e POSTGRES_USER=estudos -e POSTGRES_PASSWORD=senha-estudos -e POSTGRES_DB=estudos -v postgres-estudos-dados:/var/lib/postgresql/data postgres:17
```

Se o contêiner do banco já existir e estiver parado, use `docker start postgres-estudos`. Aguarde sua inicialização com `docker logs postgres-estudos`.

No PowerShell, dentro da pasta do projeto, inicie a API:

```powershell
docker run --rm -it --name spring-estudos --network estudos-spring -p 8081:8081 -e DB_PASSWORD=senha-estudos -e APP_CONSULTA_PASSWORD=consulta123 -e APP_ADMIN_PASSWORD=admin123 -v "${PWD}:/app" -w /app maven:3-eclipse-temurin-21-noble mvn spring-boot:run
```

Acesse `http://localhost:8081`. Para parar a aplicação, pressione Ctrl+C; para parar o banco, execute `docker stop postgres-estudos`. Os dados permanecem no volume `postgres-estudos-dados`.

Essas credenciais são exemplos públicos para estudo local, não devem ser usadas em produção. Em particular, passá-las diretamente na linha de comando pode expô-las no histórico ou na configuração do contêiner.

## API

| Método | Rota | Descrição |
| --- | --- | --- |
| GET | `/produto` | Lista produtos. |
| GET | `/produto/{id}` | Consulta um produto; retorna 404 se não existir. |
| POST | `/produto` | Salva um produto. |
| DELETE | `/produto/{id}` | Remove um produto. |
| GET | `/categoria` | Lista categorias. |
| POST | `/categoria` | Salva uma categoria. |
| GET | `/categoria/{id}/produtos` | Lista produtos de uma categoria; retorna 404 se a categoria não existir. |
| GET | `/actuator/health` | Exibe o estado da aplicação para usuários autenticados. |

Exemplo de categoria: `{"id":1,"nome":"Perifericos"}`. Exemplo de produto:

```json
{"id":1,"nome":"Teclado","preco":150.0,"categoriaId":1}
```

A categoria é opcional; se informada, precisa existir. Os IDs são informados pelo cliente e salvar com um ID existente pode atualizar o registro. Dados inválidos retornam 400. As consultas a produtos e categorias são públicas; para cadastrá-los ou excluir produtos é exigido o papel ADMIN. As operações de alteração também exigem token CSRF. O login por formulário e HTTP Basic estão disponíveis para estudo local.

Os dados ficam no PostgreSQL. O Hibernate usa `ddl-auto=update` para ajustar o esquema durante os estudos.

## Testes

Crie uma vez o banco separado para os testes de integração (não use um banco com dados importantes):

```powershell
docker exec -it postgres-estudos psql -U estudos -d estudos -c "CREATE DATABASE estudos_testes;"
```

Com o banco ligado, execute na pasta do projeto:

```powershell
docker run --rm -it --network estudos-spring -v "${PWD}:/app" -w /app maven:3-eclipse-temurin-21-noble mvn test
```

O perfil `test` usa `estudos_testes` e `create-drop`, que cria e remove tabelas nesse banco. Os testes cobrem serviços, camada web, segurança e integração com PostgreSQL.
