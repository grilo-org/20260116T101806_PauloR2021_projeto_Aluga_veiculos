# 🚗 Aluga Veículos - API REST

API REST desenvolvida em **Java com Spring Boot** para gerenciamento de **aluguel de veículos**, incluindo autenticação de usuários, reservas, controle de veículos e pagamentos.

Projeto focado em **boas práticas de arquitetura, segurança e regras de negócio**, simulando um sistema real de locação.

---

## 🧠 Funcionalidades

### 👤 Usuários
- Cadastro e autenticação
- Login com Spring Security
- Controle de acesso por usuário logado

### 🚘 Veículos
- Cadastro de veículos
- Listagem e consulta
- Controle de disponibilidade

### 📅 Reservas (Rent)
- Criação de reservas
- Atualização de datas
- Retirada e devolução de veículos
- Reserva vinculada ao usuário autenticado

### 💳 Pagamentos
- Pagamento de reservas ativas
- Validação de regras de negócio
- Métodos de pagamento (PIX - mock)
- Listagem de pagamentos por usuário
- Consulta de pagamento específico
- Status do pagamento (PENDING, PAID, CANCELED, FAILED)

---

## 🔐 Segurança
- Spring Security
- Autenticação baseada em usuário logado
- Usuários só acessam seus próprios dados
- Nenhum `userId` exposto via URL

---

## 🛠 Tecnologias Utilizadas

- **Java 21**
- **Spring Boot**
- Spring Security + JWT
- Spring Data JPA (Hibernate)
- MySQL
- Rota migratória (migrações)
- Validação de Bean
- Swagger / OpenAPI
- Arquitetura REST
- Padrão DTO
- Paginação com Pageable
- Controle de erros global (@RestControllerAdvice)

---

## 📐 Arquitetura

Projeto organizado seguindo **arquitetura em camadas**:

```
Controllers
├── Services
│ ├── Regras de negócio
├── Repositories
│ ├── Acesso a dados
├──
├── Modelos (E
├── Enums
└─
```
---
## 🔐 Autenticação & Autorização

- Autenticação via JWT
- Perfis de acesso:
  - ADMIN
  - USUÁRIO
- Controle de acesso por rota
- Usuário só acessa seus próprios dados
- Admin acessa dados globais
---
## 🌐 Exemplos de Endpoints

```
Criar Usuário - POST /auth/register
Login         - POST //auth/login

Rotas Autenticadas com Acessos ADMIN

Criar Usuários ADMIN/ COMUM - POST - /admin/user/create
Listar Usuários Por USERNAME- GET  - /admin/user/username/paulo_admin
Listar Usuários Por ID      - GET  - /admin/user/id/{id}
Listar Todos Usuários       - GET  - /admin/user

Criar Agenda de Veículos    - POST - /admin/rent
Atualizar Agenda do Veículo - PUT  - /admin/rent/1
Cadastrar Veículos          - POST - /admin/vehicle/create
Pesquisar Veículos          - GET  - /admin/vehicle/search?plate=

```
### 🔐 Pagamentos do usuário logado - ADMIN / USER
```
Criar Pagamento com PIX     - POST - /admin/payments
Criar Pagamento com Dinhero - POST - /admin/payments

json
{
  "rentId": 1,
  "method": "PIX"
}

```
---
## 💳 Módulo de Pagamentos
### Funcionalidades implementadas:
- ✅ Pagamento de reserva
- ✅ Mock de pagamento via PIX
- ✅ Outros métodos (débito / dinheiro)
- ✅ Validação de reserva ativa
- ✅ Prevenção de pagamento duplicado
- ✅ Filtro por método de pagamento
- ✅ Paginação
- ✅ Acesso restrito ao dono da reserva
---
## ⚙️ Regras de Negócio
- Só é possível pagar reservas ativas
- Usuário só pode pagar suas próprias reservas
- Pagamento está sempre vinculado a uma reserva

---
## 📄 Padrão de Resposta da API
```
{
  "status": 200,
  "message": "Payments listed successfully",
  "data": { }
}

{
  "timestamp": "2025-01-01T10:30:00",
  "status": 404,
  "error": "NOT_FOUND",
  "message": "Resource not found",
  "path": "/user/payments/my/10"
}


```
---

## 📌 Objetivo do Projeto
```
Este projeto foi desenvolvido com foco em:

Aprendizado prático de Spring Boot
Aplicação de boas práticas
Simulação de um sistema real
Consolidação de conhecimentos em API REST e segurança

```


## 👨‍💻 Autor
```
Paulo Ricardo Soares
Tecnólogo em Gestão da Tecnologia da Informação
Desenvolvedor Java | Spring Boot | Backend
```





