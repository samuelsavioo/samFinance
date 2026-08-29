# 💰 SamFinance - Assistente Financeiro Inteligente

**SamFinance** é um aplicativo Android nativo projetado para ajudar usuários a saírem do zero na organização financeira, quitarem dívidas e construírem sua primeira reserva de emergência. Através de um consultor financeiro movido por IA, o app oferece orientações personalizadas baseadas no perfil real do usuário.

---

## 🤖 IA Consciente e Responsável

Este projeto foi construído com o **auxílio de Inteligência Artificial de forma consciente e responsável**. Acreditamos que a transparência é fundamental na nova realidade do desenvolvimento de software. A IA foi utilizada para acelerar a implementação de funcionalidades complexas, garantir boas práticas de código e integrar modelos avançados de linguagem, sempre sob a supervisão humana para garantir a ética e a precisão das informações financeiras fornecidas.

---

## 🚀 Principais Funcionalidades

- **🤖 Consultor SamFinance**: Chat inteligente integrado com o **Gemini 1.5 Flash** que atua como um educador financeiro acolhedor.
- **📊 Perfil Financeiro**: Formulário detalhado para coleta de renda, gastos (fixos/variáveis), dívidas e bens.
- **🎯 Classificação de Perfil**: Lógica determinística que classifica o usuário como *Endividado*, *Estável* ou *Pronto para Investir*.
- **💾 Persistência de Dados**: 
    - **Room Database**: Histórico de conversas salvo localmente para manter o contexto.
    - **DataStore**: Armazenamento seguro de preferências e estado de login.
- **🔐 Segurança**: Proteção de chaves de API via `local.properties` e `BuildConfig`.

---

## 📂 Estrutura do Projeto

O projeto é um aplicativo Android moderno escrito em **Kotlin** com **Jetpack Compose**:

- `app/src/main/java/com/example/samfinance/ai`: Lógica de integração com o Google AI SDK.
- `app/src/main/java/com/example/samfinance/data`: Camada de dados (Room e DataStore).
- `app/src/main/java/com/example/samfinance/ui`: Interface declarativa com Compose (Screens e Temas).
- `app/src/main/java/com/example/samfinance/network`: Modelos de dados e integração com API REST.

---

## 🛠️ Tecnologias Utilizadas

- **Linguagem**: Kotlin
- **UI**: Jetpack Compose
- **IA**: Google AI SDK (Gemini API)
- **Banco de Dados**: Room
- **Persistência de Preferências**: Jetpack DataStore
- **Networking**: Retrofit & Gson
- **Injeção de Dependência**: (Próximo passo: Hilt/Koin)
- **Gerenciamento de Versão**: Git

---

## ▶️ Como Rodar o Projeto

1. **Obter uma API Key**: Consiga sua chave no [Google AI Studio](https://aistudio.google.com/).
2. **Configurar a Chave**: No arquivo `local.properties` na raiz do projeto, adicione:
   ```properties
   SAM_FINANCE_API_KEY=SUA_CHAVE_AQUI
   ```
3. **Build**: Sincronize o Gradle e execute o app em um emulador ou dispositivo físico com API 24+.

---

## 🎯 Objetivo e Filosofia

O SamFinance não é apenas um gerenciador de gastos, mas um **educador**. O foco é remover a barreira de entrada para quem não tem organização financeira, usando uma linguagem acessível e evitando jargões complexos, sempre priorizando a segurança dos dados do usuário.

---

## 📌 Autor

Samuel Sávio  
Desenvolvedor Android em evolução, explorando as fronteiras entre Mobile e IA. 🚀
