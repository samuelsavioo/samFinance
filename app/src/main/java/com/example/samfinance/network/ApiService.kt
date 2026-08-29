package com.example.samfinance.network

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

data class User(val id: Int?, val name: String, val email: String)
data class UserProfile(
    val user_id: Int,
    val income: Double,
    val housing_type: String,
    val expenses_fixed: Double,
    val expenses_variable: Double,
    val debts: Double,
    val has_vehicle: Boolean
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
        private const val BASE_URL = "http://10.0.2.2:3000/" // IP padrão para localhost no emulador Android

        fun create(): ApiService {
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ApiService::class.java)
        }
    }
}
