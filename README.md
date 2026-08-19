# Seminovos — Sistema de Gestão Operacional

API REST para controle operacional de veículos seminovos, desenvolvida com Java Spring Boot. Permite que vendedores, gerentes e administradores acompanhem em tempo real a situação de cada veículo do estoque, desde a entrada na concessionária até a liberação para venda.

---

## Tecnologias

- Java 21
- Spring Boot 4.x
- Spring Security + JWT (JJWT)
- Spring Data JPA / Hibernate
- PostgreSQL (produção) / H2 (desenvolvimento)
- SpringDoc OpenAPI (Swagger UI)
- Lombok
- Maven

---

## Funcionalidades

- Cadastro e consulta de veículos por placa, modelo e status
- Controle de revisão mecânica e higienização (H3)
- Registro de serviços com fornecedores especializados
- Atualização automática de status do veículo conforme etapas concluídas
- Controle de acesso por perfil (Admin, Gerente, Vendedor)
- Dashboard operacional com indicadores do estoque
- Documentação interativa via Swagger UI

---

## Fluxo Operacional

```
Veículo entra no estoque
        ↓
   EM_PREPARACAO  →  Serviços cadastrados e em andamento
        ↓
   Revisão concluída + H3 concluído + Serviços concluídos
        ↓
   PRONTO_PARA_VENDA
        ↓
      VENDIDO
```

---

## Perfis de Acesso

| Funcionalidade | ADMIN | GERENTE | VENDEDOR |
|---|---|---|---|
| Cadastrar usuários | ✅ | ✅ | ❌ |
| Gerenciar veículos | ✅ | ✅ | ❌ |
| Consultar veículos | ✅ | ✅ | ✅ |
| Cadastrar serviços | ✅ | ✅ | ❌ |
| Finalizar revisão/H3 | ✅ | ✅ | ❌ |
| Acessar dashboard | ✅ | ✅ | ❌ |

---

## Como executar

### Pré-requisitos

- Java 21+
- Maven
- PostgreSQL (para produção)

### Desenvolvimento (H2 em memória)

```bash
# Clone o repositório
git clone https://github.com/Danielss44/seminivos.git
cd seminivos

# Execute com perfil de desenvolvimento
./mvnw spring-boot:run
```

O perfil `dev` usa H2 em memória — nenhuma instalação de banco necessária.

### Produção (PostgreSQL)

Crie o banco de dados:
```sql
CREATE DATABASE seminovos;
```

Configure o `application-prod.properties`:
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/seminovos
spring.datasource.username=seu_usuario
spring.datasource.password=sua_senha
api.security.token.secret=sua_chave_secreta
app.admin.login=admin
app.admin.senha=sua_senha_admin
```

Ative o perfil:
```properties
# application.properties
spring.profiles.active=prod
```

---

## Documentação da API

Com a aplicação rodando, acesse:

```
http://localhost:8080/swagger-ui/index.html
```

### Autenticação

1. Faça `POST /auth/login` com as credenciais
2. Copie o token retornado
3. Clique em **Authorize** no Swagger e cole o token

Na primeira execução, o sistema cria automaticamente um usuário administrador com as credenciais definidas no `application.properties`.

---

## Estrutura do Projeto

```
src/main/java/com/Danielss44/seminovos/
├── config/          # Configurações (Swagger)
├── controllers/     # Endpoints REST
├── DTO/             # Objetos de transferência de dados
├── exception/       # Exceções customizadas e handler global
├── model/           # Entidades JPA e enums
├── repository/      # Repositórios Spring Data
├── security/        # JWT, filtros e configuração de segurança
└── services/        # Regras de negócio
```

---

## Principais Endpoints

| Método | Rota | Descrição |
|---|---|---|
| POST | `/auth/login` | Autenticação |
| POST | `/auth/cadastrar` | Cadastrar usuário |
| GET | `/veiculos` | Listar veículos |
| POST | `/veiculos` | Cadastrar veículo |
| GET | `/veiculos/placa/{placa}` | Buscar por placa |
| POST | `/revisoes` | Criar revisão |
| PATCH | `/revisoes/finalizar/{placa}` | Finalizar revisão |
| POST | `/h3` | Criar H3 |
| PATCH | `/h3/finalizar/{placa}` | Finalizar H3 |
| POST | `/servicos` | Cadastrar serviço |
| PATCH | `/servicos/concluir/{id}` | Concluir serviço |
| GET | `/dashboard` | Dashboard operacional |

---

## Status do Projeto

- [x] Backend completo
- [ ] Frontend (em desenvolvimento)
