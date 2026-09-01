package com.example.samfinance.network

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

data class User(val id: Int?, val name: String, val email: String)

data class TransactionItem(
    val id: String = java.util.UUID.randomUUID().toString(),
    val title: String,
    val category: String,
    val amount: Double,
    val isIncome: Boolean,
    val date: String = "Hoje"
)

data class UserProfile(
    val user_id: Int = 1,
    val income: Double = 0.0,
    val housing_type: String = "Própria",
    val vehicle_type: String = "Nenhum",
    val dependents: Int = 0,
    val primary_goal: String = "Reserva de Emergência",
    val expenses_fixed: Double = 0.0,
    val expenses_variable: Double = 0.0,
    val debts: Double = 0.0,
    val has_vehicle: Boolean = vehicle_type != "Nenhum"
) {
    fun classify(): String {
        val totalExpenses = expenses_fixed + expenses_variable
        return when {
            debts > 0 || totalExpenses > income -> "Endividado"
            income > totalExpenses && debts == 0.0 -> {
                if (income - totalExpenses > income * 0.2) "Pronto para investir"
                else "Estável"
            }
            else -> "Estável"
        }
    }
}

interface ApiService {
    @GET("/users")
    suspend fun getUsers(): List<User>

    @POST("/profiles")
    suspend fun updateProfile(@Body profile: UserProfile): Map<String, String>

    @GET("/profiles/{userId}")
    suspend fun getProfile(@Path("userId") userId: Int): UserProfile

    companion object {
        private const val BASE_URL = "http://10.0.2.2:3000/"

        fun create(): ApiService {
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ApiService::class.java)
        }
    }
}
