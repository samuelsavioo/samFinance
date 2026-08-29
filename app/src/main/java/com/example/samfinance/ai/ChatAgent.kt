package com.example.samfinance.ai

import com.example.samfinance.BuildConfig
import com.example.samfinance.network.UserProfile
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.Content
import com.google.ai.client.generativeai.type.content

class ChatAgent(history: List<Content> = emptyList()) {
    private val generativeModel = GenerativeModel(
        modelName = "gemini-1.5-flash",
        apiKey = BuildConfig.SAM_FINANCE_API_KEY,
        systemInstruction = content { 
            text("""
                Você é o SamFinance, um consultor financeiro iniciante, educativo e acolhedor. Seu objetivo é ajudar pessoas que não têm organização financeira prévia a saírem do zero, quitarem suas dívidas e montarem sua primeira reserva de emergência.

                REGRAS:
                1. Sempre baseie suas respostas nos "Dados do Usuário" e na "Classificação do sistema" (Endividado, Estável ou Pronto) fornecidos no contexto.
                2. Comunique-se de forma acessível e amigável, sem usar jargões financeiros complexos. Inicie com uma saudação acolhedora (ex: "Olá! Sou o Sam...").
                3. Nunca invente informações financeiras e não faça recomendações de investimentos específicos. Se o usuário quiser investir, atue apenas de forma educativa (explicando o que é CDB, Tesouro Direto, etc.).
                4. Personalize a resposta com base nos bens da pessoa: 
                   - Se possuir veículo (carro/moto), lembre-a de considerar gastos sazonais como IPVA, pneus e manutenção periódica no orçamento.
                   - Se morar de aluguel, reforce a importância de uma reserva um pouco maior para garantir a moradia em imprevistos.
                5. Se a pessoa estiver "Endividada", mantenha um tom encorajador e focado em renegociação e corte de gastos. Nunca seja punitivo.
                6. Se a pergunta fugir do escopo de finanças pessoais básicas, informe educadamente sua limitação.
            """.trimIndent())
        }
    )

    private val chat = generativeModel.startChat(history = history)

    suspend fun sendMessage(prompt: String, userProfile: UserProfile? = null): String? {
        val contextPrompt = if (userProfile != null) {
            """
                Dados do Usuário:
                - Renda mensal: R${'$'} ${userProfile.income}
                - Gastos fixos: R${'$'} ${userProfile.expenses_fixed}
                - Gastos variáveis: R${'$'} ${userProfile.expenses_variable}
                - Dívidas: R${'$'} ${userProfile.debts}
                - Possui veículo: ${if (userProfile.has_vehicle) "Sim" else "Não"}
                - Moradia: ${userProfile.housing_type}

                Classificação do Perfil:
                - ${userProfile.classify()}

                Pergunta do usuário: $prompt
            """.trimIndent()
        } else {
            prompt
        }

        return try {
            val response = chat.sendMessage(contextPrompt)
            response.text
        } catch (e: Exception) {
            "Desculpe, ocorreu um erro ao processar sua solicitação: ${e.message}"
        }
    }
}
