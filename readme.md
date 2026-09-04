# SmartStock Backend

API REST para gerenciamento de estoque, produtos e movimentações, desenvolvida com Java e Spring Boot.

O backend do SmartStock fornece autenticação JWT, controle de acesso por perfil, gerenciamento de produtos, controle de entradas e saídas de estoque, identificação de produtos com estoque baixo, paginação, validações, documentação com Swagger e persistência em PostgreSQL.

## 🌐 Aplicação em produção

**API:**
https://smartstock-api-4jx3.onrender.com

**Swagger / OpenAPI:**
https://smartstock-api-4jx3.onrender.com/swagger-ui.html

## 🚀 Tecnologias

* Java
* Spring Boot
* Spring Web
* Spring Data JPA
* Spring Security
* JWT
* PostgreSQL
* Flyway
* Bean Validation
* Lombok
* SpringDoc OpenAPI / Swagger
* Maven
* Docker
* JUnit
* Mockito

## 📌 Funcionalidades

### Autenticação e segurança

* Cadastro de usuários
* Login com e-mail e senha
* Autenticação utilizando JWT
* Senhas criptografadas com BCrypt
* Controle de acesso por perfil
* Perfis `USER` e `ADMIN`
* Proteção de endpoints com Spring Security
* Configuração CORS para frontend local e produção

### Produtos

* Cadastro de produtos
* Listagem paginada
* Busca por ID
* Atualização de informações
* Exclusão de produtos
* Validação de código único
* Consulta de produtos com estoque baixo

Após o cadastro inicial, a quantidade de um produto não pode ser alterada diretamente pela edição do produto.

Toda mudança no estoque deve ser registrada por meio de uma movimentação.

### Movimentações

* Registro de entrada de estoque (`ENTRY`)
* Registro de saída de estoque (`EXIT`)
* Validação de estoque disponível
* Histórico de movimentações
* Consulta de movimentações por produto
* Listagem paginada
* Ordenação pelas movimentações mais recentes

## 🧠 Regras de negócio

O SmartStock mantém o histórico das alterações realizadas no estoque.

Uma entrada aumenta a quantidade disponível:

```text
Estoque atual + quantidade da movimentação
```

Uma saída reduz a quantidade:

```text
Estoque atual - quantidade da movimentação
```

Caso uma saída seja maior que a quantidade disponível, a operação é rejeitada.

Produtos são considerados com estoque baixo quando:

```text
quantity <= minimumStock
```

## 🔐 Permissões

| Operação                 | USER | ADMIN |
| ------------------------ | :--: | :---: |
| Visualizar produtos      |   ✅  |   ✅   |
| Visualizar movimentações |   ✅  |   ✅   |
| Registrar movimentações  |   ✅  |   ✅   |
| Visualizar estoque baixo |   ✅  |   ✅   |
| Criar produtos           |   ❌  |   ✅   |
| Editar produtos          |   ❌  |   ✅   |
| Excluir produtos         |   ❌  |   ✅   |

## 📂 Estrutura principal

```text
src/main/java/com/kleberson/SmartStock
├── config
├── controller
├── dto
├── entity
├── enums
├── exception
├── repository
├── service
└── SmartStockApplication.java
```

## 🗄️ Banco de dados

O projeto utiliza PostgreSQL.

A estrutura do banco é versionada com Flyway, permitindo reproduzir as alterações do schema automaticamente em diferentes ambientes.

Principais entidades:

```text
User
Product
Movement
```

Relacionamento principal:

```text
Product 1 ─────── N Movement
```

## 🔄 Fluxo de movimentação

```text
Cliente
   ↓
POST /api/movements
   ↓
MovementController
   ↓
MovementService
   ↓
Busca produto
   ↓
Valida operação
   ↓
Atualiza estoque
   ↓
Registra movimentação
   ↓
PostgreSQL
```

## 🔑 Autenticação

Após realizar login, a API retorna um JWT.

Exemplo:

```http
POST /api/auth/login
```

```json
{
  "email": "usuario@email.com",
  "password": "senha"
}
```

O token deve ser enviado nas requisições protegidas:

```http
Authorization: Bearer SEU_TOKEN
```

O Swagger também possui suporte ao Bearer Token por meio do botão **Authorize**.

## 📄 Principais endpoints

### Autenticação

```http
POST /api/auth/register
POST /api/auth/login
```

### Produtos

```http
GET    /api/products
GET    /api/products/{id}
POST   /api/products
PUT    /api/products/{id}
DELETE /api/products/{id}

GET /api/products/low-stock
```

### Movimentações

```http
GET  /api/movements
POST /api/movements

GET /api/movements/product/{productId}
```

## 📑 Paginação

Produtos e movimentações possuem paginação.

Exemplo:

```http
GET /api/products?page=0&size=10
```

```http
GET /api/movements?page=0&size=10
```

A resposta segue o padrão de `Page` do Spring Data:

```json
{
  "content": [],
  "totalElements": 0,
  "totalPages": 0,
  "size": 10,
  "number": 0,
  "first": true,
  "last": true
}
```

## ⚠️ Tratamento de erros

A API possui tratamento global de exceções.

Entre os principais cenários tratados estão:

* Produto não encontrado
* Produto com código duplicado
* E-mail já cadastrado
* Credenciais inválidas
* Estoque insuficiente
* Dados inválidos
* Acesso não autorizado

Exemplo:

```json
{
  "status": 409,
  "message": "Insufficient stock",
  "timestamp": "2026-09-04T10:00:00"
}
```

## 🧪 Testes

O projeto possui testes unitários utilizando JUnit e Mockito.

Foram implementados testes para:

* Cadastro de produto
* Código de produto duplicado
* Busca de produto
* Atualização de produto
* Exclusão de produto
* Estoque baixo
* Entrada de estoque
* Saída de estoque
* Estoque insuficiente
* Produto inexistente em movimentações

Para executar:

```bash
./mvnw test
```

No Windows:

```bash
mvnw.cmd test
```

## ▶️ Executando localmente

### Pré-requisitos

* Java
* Docker
* Git

Clone o projeto:

```bash
git clone https://github.com/kleberson154/SmartStockBackEnd.git
```

Entre na pasta:

```bash
cd SmartStockBackEnd
```

Configure um PostgreSQL local.

Exemplo:

```text
Database: smartstock
Username: postgres
Password: postgres
Port: 5432
```

Execute:

```bash
./mvnw spring-boot:run
```

No Windows:

```bash
mvnw.cmd spring-boot:run
```

A aplicação ficará disponível em:

```text
http://localhost:8080
```

Swagger:

```text
http://localhost:8080/swagger-ui.html
```

## ⚙️ Variáveis de ambiente

Em produção, o projeto utiliza variáveis de ambiente:

```env
SPRING_PROFILES_ACTIVE=prod

DB_URL=jdbc:postgresql://host:5432/database
DB_USERNAME=username
DB_PASSWORD=password

JWT_SECRET=your-secret
JWT_EXPIRATION=86400000
```

Nenhuma credencial de produção deve ser versionada no GitHub.

## 🐳 Docker

O backend possui suporte a Docker e é utilizado dessa forma no ambiente de produção.

Build:

```bash
docker build -t smartstock-api .
```

Execução:

```bash
docker run -p 8080:8080 smartstock-api
```

## ☁️ Deploy

A aplicação está distribuída da seguinte forma:

```text
React / Vercel
      ↓
SmartStock API / Render
      ↓
PostgreSQL / Render
```

O Flyway executa as migrations automaticamente durante a inicialização do backend.

## 🎯 Objetivo do projeto

O SmartStock foi desenvolvido como projeto de portfólio com o objetivo de aplicar conceitos utilizados em aplicações backend reais, incluindo:

* APIs REST
* Arquitetura em camadas
* Autenticação e autorização
* Regras de negócio
* Persistência de dados
* Migrations
* Tratamento global de erros
* Validações
* Paginação
* Testes
* Containerização
* Deploy em cloud
* Integração frontend/backend

## 👨‍💻 Autor

**Kleberson Andrade**

GitHub:
https://github.com/kleberson154

## 📜 Licença

Projeto desenvolvido para fins de estudo e portfólio.
