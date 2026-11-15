# Sistema Aliança

API REST desenvolvida com Java e Spring Boot para cadastro, autenticação e gerenciamento de usuários.

## Tecnologias

- Java 21
- Spring Boot
- Spring Web MVC
- Spring Data JPA
- Spring Security
- JWT
- PostgreSQL
- Docker
- Maven
- Bean Validation
- BCrypt

## Funcionalidades

- Cadastro de usuários
- Validação dos dados de entrada
- Senhas criptografadas com BCrypt
- Login com geração de token JWT
- Rotas protegidas
- Consulta de usuários
- Consulta do usuário autenticado
- Atualização do nome do usuário autenticado
- Tratamento global de exceções

## Arquitetura

```text
controller  → recebe as requisições HTTP
service     → executa as regras de negócio
repository  → acessa o banco de dados
model       → representa as entidades
dto         → transporta os dados da API
exception   → trata os erros da aplicação
config      → configura a segurança
```

## Como executar

### Pré-requisitos

- Java 21
- Docker Desktop

### 1. Configurar as variáveis de ambiente

Na raiz do projeto, copie o arquivo de exemplo:

```powershell
Copy-Item .env.example .env
```

Edite o arquivo `.env` e defina `DB_PASSWORD` e `JWT_SECRET`. A chave JWT deve ser aleatória e possuir pelo menos 32 bytes. Uma forma de gerar uma chave em Base64 no PowerShell (com OpenSSL instalado) é:

```powershell
openssl rand -base64 32
```

Cole o resultado em `JWT_SECRET`. Use uma senha local forte em `DB_PASSWORD`. O arquivo `.env` é ignorado pelo Git e não deve ser publicado.

### 2. Iniciar o PostgreSQL

Na raiz do projeto:

```powershell
docker compose up -d
```

### 3. Executar a aplicação

No Windows, com o `.env` preenchido:

```powershell
.\mvnw.cmd spring-boot:run
```

A aplicação ficará disponível em:

```text
http://localhost:8080
```

## Rotas

| Método | Rota | Autenticação | Descrição |
|---|---|---|---|
| POST | `/users` | Não | Cadastra um usuário |
| POST | `/users/login` | Não | Realiza o login e retorna um JWT |
| GET | `/users` | Sim | Lista os usuários |
| GET | `/users/{id}` | Sim | Busca um usuário pelo ID |
| GET | `/users/me` | Sim | Retorna o usuário autenticado |
| PATCH | `/users/me` | Sim | Atualiza o nome do usuário autenticado |

## Exemplo de cadastro

```json
{
  "name": "Usuario Teste",
  "email": "usuario@exemplo.com",
  "password": "senhaDeTeste123"
}
```

## Exemplo de login

```json
{
  "email": "usuario@exemplo.com",
  "password": "senhaDeTeste123"
}
```

O login retorna um token JWT:

```json
{
  "token": "token-jwt"
}
```

Nas rotas protegidas, envie o token no cabeçalho:

```text
Authorization: Bearer token-jwt
```

## Respostas de erro

| Status | Significado |
|---|---|
| 400 | Dados inválidos |
| 401 | Credenciais inválidas ou token ausente |
| 404 | Usuário não encontrado |
| 409 | E-mail já cadastrado |

## Escopo do projeto

Este projeto tem finalidade educacional e de demonstração de competências em desenvolvimento de APIs com Java e Spring Boot. Ele não pretende ser uma solução pronta para produção. As rotas `GET /users` e `GET /users/{id}` permitem que qualquer usuário autenticado consulte os registros, sem distinção de perfis ou papéis. Essa escolha mantém o escopo simples para a demonstração; em um sistema real, seria necessário definir regras de autorização conforme os requisitos do negócio.

## Configuração e segurança

- Credenciais do banco e a chave JWT são fornecidas por variáveis de ambiente ou pelo arquivo local `.env`.
- O arquivo `.env` não deve ser versionado. Use `.env.example` como modelo.
- A aplicação não possui uma chave JWT padrão: ela não inicia se `JWT_SECRET` estiver ausente ou tiver menos de 32 bytes.
- O PostgreSQL do Docker é publicado somente no endereço local (`127.0.0.1`).
- `spring.jpa.show-sql` fica desativado para evitar registrar dados das consultas desnecessariamente.

**Nunca publique senhas, tokens, chaves privadas ou arquivos `.env`.** Se uma credencial real já tiver sido compartilhada ou publicada, troque-a; apagar o arquivo de um commit posterior não remove o segredo do histórico do Git.
