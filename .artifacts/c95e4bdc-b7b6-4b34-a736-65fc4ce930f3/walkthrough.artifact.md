# Walkthrough: Integração SamFinance Android & API

Implementei a estrutura base para o aplicativo Android e expandi a API Node.js para suportar os dados de perfil do usuário.

## Mudanças Realizadas

### Backend (Node.js)
- **Novas Rotas:** Adicionadas rotas `POST /profiles` e `GET /profiles/:userId` no [index.js](file:///C:/Users/samue/StudioProjects/samFinance/OneDrive/Documentos/Programação/SamFinance/index.js).
- **Lógica de Perfil:** Implementado o `ON DUPLICATE KEY UPDATE` para permitir que o usuário atualize seus dados de renda e moradia.

### Android (App Nativo)
- **Configuração Gradle:** Projeto configurado com Kotlin, Jetpack Compose e Retrofit.
- **Permissões:** Adicionada permissão de INTERNET no `AndroidManifest.xml`.
- **Rede:** Criada a interface [ApiService.kt](file:///C:/Users/samue/StudioProjects/samFinance/app/src/main/java/com/example/samfinance/network/ApiService.kt) para centralizar as chamadas à API.
- **UI Base:** [MainActivity.kt](file:///C:/Users/samue/StudioProjects/samFinance/app/src/main/java/com/example/samfinance/MainActivity.kt) preparada com Jetpack Compose.

## Próximos Passos (Recomendação)
1. **Banco de Dados:** Execute o comando SQL abaixo no seu MySQL para criar a tabela de perfis:
```sql
CREATE TABLE profiles (
    user_id INT PRIMARY KEY,
    income DECIMAL(10,2),
    housing_type VARCHAR(50),
    expenses_fixed DECIMAL(10,2),
    expenses_variable DECIMAL(10,2),
    FOREIGN KEY (user_id) REFERENCES users(id)
);
```
2. **Sincronização:** Abra o projeto no Android Studio e clique em "Sync Project with Gradle Files".
