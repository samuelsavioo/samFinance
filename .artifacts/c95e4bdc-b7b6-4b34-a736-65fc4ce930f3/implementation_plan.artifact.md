# Plano de Implementação: SamFinance Android e Evolução da API Node.js

Este plano detalha a criação da estrutura base de um aplicativo Android nativo utilizando Kotlin e Jetpack Compose, integrando-o à API Node.js existente via Retrofit. Também inclui a expansão da API Node.js para suportar informações de perfil do usuário.

## Mudanças Propostas

### 1. Evolução da API Node.js

Adicionar rotas para gerenciar o perfil do usuário (renda, moradia, etc.).

#### [MODIFY] [index.js](file:///C:/Users/samue/StudioProjects/samFinance/OneDrive/Documentos/Programação/SamFinance/index.js)
- Adicionar rotas `POST /profiles` e `GET /profiles/:userId`.
- (Nota: Será necessário criar uma tabela `profiles` no MySQL).

### 2. Projeto Android (Novo)

Criar a estrutura base do projeto Android no diretório raiz.

#### [NEW] [settings.gradle.kts](file:///C:/Users/samue/StudioProjects/samFinance/settings.gradle.kts)
- Configuração de repositórios e nome do projeto.

#### [NEW] [build.gradle.kts](file:///C:/Users/samue/StudioProjects/samFinance/build.gradle.kts)
- Configuração de plugins (Android, Kotlin, Compose).

#### [NEW] [app/build.gradle.kts](file:///C:/Users/samue/StudioProjects/samFinance/app/build.gradle.kts)
- Configuração do módulo `app`.
- **Adição do Retrofit e Gson**.
- Configuração do Jetpack Compose.

#### [NEW] [AndroidManifest.xml](file:///C:/Users/samue/StudioProjects/samFinance/app/src/main/AndroidManifest.xml)
- Declaração da atividade principal e permissão de INTERNET.

#### [NEW] [MainActivity.kt](file:///C:/Users/samue/StudioProjects/samFinance/app/src/main/java/com/example/samfinance/MainActivity.kt)
- Ponto de entrada do app com Jetpack Compose.

#### [NEW] [ApiService.kt](file:///C:/Users/samue/StudioProjects/samFinance/app/src/main/java/com/example/samfinance/network/ApiService.kt)
- Interface Retrofit para comunicação com o backend.

## Plano de Verificação

### Verificação Manual
- Validar se o `build.gradle` sincroniza corretamente após a criação dos arquivos.
- Testar as novas rotas do Node.js usando uma ferramenta como Postman ou Curl.
- Compilar o app Android para garantir que as dependências do Retrofit foram resolvidas.
