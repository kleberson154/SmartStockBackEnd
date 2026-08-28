# SmartStock API

API REST para gerenciamento de estoque desenvolvida com **Java e Spring Boot**.

O SmartStock permite gerenciar produtos, controlar entradas e saídas de estoque, consultar produtos com estoque baixo e
manter um histórico de movimentações. A aplicação também possui autenticação baseada em **JWT** e controle de acesso por
perfis de usuário.

## 🚀 Tecnologias

* Java
* Spring Boot
* Spring Web
* Spring Data JPA
* Spring Security
* JWT
* PostgreSQL
* Flyway
* Docker
* Maven
* Bean Validation
* Swagger / OpenAPI
* JUnit 5
* Mockito

## 📦 Funcionalidades

### Produtos

* Cadastro de produtos
* Listagem de produtos
* Busca de produto por ID
* Atualização de produtos
* Exclusão de produtos
* Validação de código único
* Consulta de produtos com estoque baixo

### Movimentações de estoque

O estoque é atualizado através de movimentações.

**ENTRY**

* Registra entrada de produtos
* Aumenta automaticamente a quantidade disponível

**EXIT**

* Registra saída de produtos
* Reduz automaticamente a quantidade disponível
* Impede saídas maiores que o estoque atual

Também é possível consultar:

* todas as movimentações;
* histórico de movimentações de um produto específico.

## 🔐 Autenticação e autorização

A API utiliza autenticação baseada em **JWT (JSON Web Token)**.

Existem dois níveis de acesso:

### USER

Pode:

* consultar produtos;
* consultar produtos com estoque baixo;
* registrar movimentações;
* consultar histórico de movimentações.

### ADMIN

Possui as permissões de USER e também pode:

* cadastrar produtos;
* atualizar produtos;
* excluir produtos.

Novos usuários cadastrados pela API recebem automaticamente o perfil `USER`.

## 🛣️ Principais endpoints

### Autenticação

| Método | Endpoint             | Descrição         |
|--------|----------------------|-------------------|
| POST   | `/api/auth/register` | Cadastrar usuário |
| POST   | `/api/auth/login`    | Realizar login    |

### Produtos

| Método | Endpoint                  | Permissão    | Descrição                  |
|--------|---------------------------|--------------|----------------------------|
| GET    | `/api/products`           | USER / ADMIN | Listar produtos            |
| GET    | `/api/products/{id}`      | USER / ADMIN | Buscar produto             |
| GET    | `/api/products/low-stock` | USER / ADMIN | Produtos com estoque baixo |
| POST   | `/api/products`           | ADMIN        | Cadastrar produto          |
| PUT    | `/api/products/{id}`      | ADMIN        | Atualizar produto          |
| DELETE | `/api/products/{id}`      | ADMIN        | Excluir produto            |

### Movimentações

| Método | Endpoint                             | Permissão    | Descrição              |
|--------|--------------------------------------|--------------|------------------------|
| GET    | `/api/movements`                     | USER / ADMIN | Listar movimentações   |
| GET    | `/api/movements/product/{productId}` | USER / ADMIN | Histórico do produto   |
| POST   | `/api/movements`                     | USER / ADMIN | Registrar movimentação |

## 📖 Swagger / OpenAPI

Com a aplicação em execução, a documentação interativa da API pode ser acessada pelo Swagger UI:

```text
http://localhost:8080/swagger
```

Para testar endpoints protegidos:

1. faça login em `/api/auth/login`;
2. copie o JWT retornado;
3. clique em **Authorize**;
4. informe o token;
5. execute os endpoints protegidos.

O Swagger adicionará automaticamente o header:

```text
Authorization: Bearer <token>
```

## 🗄️ Banco de dados

O projeto utiliza **PostgreSQL**.

Durante o desenvolvimento, o banco pode ser executado através do Docker.

Exemplo:

```bash
docker run \
  --name smartstock-postgres \
  -e POSTGRES_DB=smartstock \
  -e POSTGRES_USER=postgres \
  -e POSTGRES_PASSWORD=postgres \
  -p 5432:5432 \
  -d postgres
```

## 🔄 Flyway

O versionamento do banco de dados é realizado com **Flyway**.

As migrations estão localizadas em:

```text
src/main/resources/db/migration
```

Ao iniciar a aplicação, o Flyway verifica e aplica automaticamente as migrations pendentes.

## ⚙️ Configuração

O projeto possui perfis separados para desenvolvimento e produção:

```text
application.yml
application-dev.yml
application-prod.yml
```

Por padrão, a aplicação utiliza o perfil `dev`.

O perfil pode ser alterado através da variável:

```text
SPRING_PROFILES_ACTIVE=prod
```

### Variáveis de ambiente de produção

Configure:

```text
DB_URL
DB_USERNAME
DB_PASSWORD
JWT_SECRET
```

Variáveis opcionais:

```text
PORT
JWT_EXPIRATION
```

Nenhuma credencial de produção deve ser armazenada no repositório.

## ▶️ Executando o projeto

### Pré-requisitos

* Java
* Docker
* Git

Clone o repositório:

```bash
git clone https://github.com/kleberson154/SmartStockBackEnd.git
```

Entre na pasta:

```bash
cd SmartStockBackEnd
```

Inicie o PostgreSQL.

Depois execute:

### Windows

```powershell
.\mvnw.cmd spring-boot:run
```

### Linux / macOS

```bash
./mvnw spring-boot:run
```

A API estará disponível em:

```text
http://localhost:8080
```

## 🧪 Testes

O projeto possui testes unitários utilizando **JUnit 5 e Mockito** para validar as principais regras de negócio.

Entre os cenários testados estão:

* criação de produtos;
* código de produto duplicado;
* atualização de produtos;
* produto inexistente;
* entrada de estoque;
* saída de estoque;
* tentativa de saída com estoque insuficiente;
* movimentação para produto inexistente;
* exclusão de produtos;
* consulta de estoque baixo.

Execute os testes:

### Windows

```powershell
.\mvnw.cmd test
```

### Linux / macOS

```bash
./mvnw test
```

Para validar e gerar o pacote completo:

```bash
./mvnw clean package
```

## 🧱 Arquitetura

O backend segue uma separação em camadas:

```text
Controller
    ↓
Service
    ↓
Repository
    ↓
PostgreSQL
```

A aplicação também utiliza DTOs para separar os modelos persistidos das estruturas utilizadas nas requisições e
respostas da API.

Estrutura principal:

```text
src/main/java/com/kleberson/SmartStock
├── config
├── controller
├── dto
├── entity
├── enums
├── exception
├── repository
├── security
└── service
```

## ⚠️ Tratamento de erros

A API possui tratamento global de exceções e retorna respostas HTTP adequadas para diferentes situações, incluindo:

* `400 Bad Request` — dados de entrada inválidos;
* `401 Unauthorized` — credenciais inválidas;
* `403 Forbidden` — usuário sem permissão;
* `404 Not Found` — recurso não encontrado;
* `409 Conflict` — conflito de dados, estoque insuficiente ou recurso duplicado.

## 🎯 Objetivo do projeto

O SmartStock foi desenvolvido como projeto de portfólio com o objetivo de aplicar conceitos utilizados no
desenvolvimento de APIs REST profissionais, incluindo:

* arquitetura em camadas;
* regras de negócio;
* persistência com JPA/Hibernate;
* migrations de banco de dados;
* autenticação JWT;
* autorização baseada em roles;
* validação de dados;
* tratamento global de exceções;
* documentação de API;
* testes unitários;
* configuração separada por ambiente.

## 👨‍💻 Autor

**Kleberson Andrade**

GitHub: `kleberson154`
