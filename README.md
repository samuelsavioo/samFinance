# 💰 SamFinance API

API REST para gerenciamento de finanças pessoais.

O **SamFinance** é uma aplicação backend desenvolvida com Node.js e MySQL que permite o cadastro e gerenciamento de:

- 👤 Usuários  
- 📂 Categorias (receitas e despesas)  
- 💳 Transações financeiras  

Projeto criado com foco em aprendizado de APIs RESTful e integração com banco relacional.

---

# 🚀 Tecnologias Utilizadas

- Node.js
- Express
- MySQL
- mysql2
- Nodemon (ambiente de desenvolvimento)

---

# 📂 Estrutura Atual do Projeto

samFinance/

├── index.js       # Arquivo principal da aplicação  
├── db.js          # Configuração do pool de conexões MySQL  
├── package.json  
└── README.md  

---

# ⚙️ Configuração do Banco de Dados

Banco utilizado: **MySQL**

Nome do banco:
sam_finance

Tabelas necessárias:

- users
- categories
- transactions

---

# ▶️ Como Rodar o Projeto

## 1️⃣ Clonar o repositório

```bash
git clone https://github.com/samuelsavioo/samFinance.git
cd samFinance
```

## 2️⃣ Instalar dependências

```bash
npm install
```

## 3️⃣ Configurar banco de dados

No arquivo `db.js`, configure suas credenciais:

```js
host: 'localhost',
user: 'root',
password: 'SUA_SENHA',
database: 'sam_finance'
```

⚠️ Recomendado: utilizar variáveis de ambiente (.env).

## 4️⃣ Executar o servidor

```bash
npm run dev
```

Servidor rodando em:

http://localhost:3000

---

# 📌 Endpoints Disponíveis

## 👤 Usuários

GET    /users  
POST   /users  
DELETE /users/:id  

### Exemplo JSON

```json
{
  "name": "Samuel",
  "email": "samuel@email.com",
  "password": "123456"
}
```

---

## 📂 Categorias

GET    /categories  
POST   /categories  
DELETE /categories/:id  

### Exemplo JSON

```json
{
  "users_id": 1,
  "name": "Alimentação",
  "type": "expense"
}
```

---

## 💳 Transações

GET    /transactions  
POST   /transactions  
DELETE /transactions/:id  

### Exemplo JSON

```json
{
  "users_id": 1,
  "category_id": 2,
  "description": "Supermercado",
  "amount": 150.75,
  "date": "2026-02-19"
}
```

---

# 🎯 Objetivos do Projeto

- Construção de API REST
- CRUD completo com MySQL
- Uso de pool de conexões
- Evolução futura para arquitetura em camadas

---

# 🔮 Próximas Melhorias

- Hash de senha com bcrypt
- Autenticação JWT
- Separação em Controllers / Services
- Uso de variáveis de ambiente
- Docker
- Testes automatizados

---

# 📌 Autor

Samuel Sávio  
Desenvolvedor Backend em evolução 🚀
