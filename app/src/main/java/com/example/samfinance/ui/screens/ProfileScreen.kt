package com.example.samfinance.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.samfinance.data.SessionManager
import com.example.samfinance.network.UserProfile
import com.example.samfinance.ui.theme.*
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    onSaveSuccess: () -> Unit = {},
    onLogout: () -> Unit = {}
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val sessionManager = remember { SessionManager(context) }
    val savedProfile by sessionManager.userProfile.collectAsState(initial = null)

    var income by remember { mutableStateOf("") }
    var housingType by remember { mutableStateOf("Própria") }
    var vehicleType by remember { mutableStateOf("Nenhum") }
    var dependentsText by remember { mutableStateOf("0") }
    var primaryGoal by remember { mutableStateOf("Sair das dívidas") }
    var fixedExpenses by remember { mutableStateOf("") }
    var variableExpenses by remember { mutableStateOf("") }
    var debts by remember { mutableStateOf("") }

    LaunchedEffect(savedProfile) {
        savedProfile?.let {
            income = if (it.income > 0) it.income.toString() else ""
            housingType = it.housing_type
            vehicleType = it.vehicle_type
            dependentsText = it.dependents.toString()
            primaryGoal = it.primary_goal
            fixedExpenses = if (it.expenses_fixed > 0) it.expenses_fixed.toString() else ""
            variableExpenses = if (it.expenses_variable > 0) it.expenses_variable.toString() else ""
            debts = if (it.debts > 0) it.debts.toString() else ""
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundDeepBlack)
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            text = "Perfil Financeiro",
            style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
            color = TextPrimaryLight
        )
        Text(
            text = "Essas informações ajudam o SamFinance a dar conselhos precisos.",
            color = TextSecondaryMuted,
            style = MaterialTheme.typography.bodySmall
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Renda
        OutlinedTextField(
            value = income,
            onValueChange = { income = it },
            label = { Text("Renda Mensal Líquida (R$)") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Moradia Selection
        Text("Moradia", fontWeight = FontWeight.Bold, color = TextPrimaryLight)
        Spacer(modifier = Modifier.height(8.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            listOf("Própria", "Alugada", "Financiada", "Com pais").forEach { option ->
                FilterChip(
                    selected = housingType == option,
                    onClick = { housingType = option },
                    label = { Text(option) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = ElectricBluePrimary,
                        selectedLabelColor = BackgroundDeepBlack,
                        containerColor = GlassCard,
                        labelColor = TextPrimaryLight
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Veículos Selection
        Text("Veículos", fontWeight = FontWeight.Bold, color = TextPrimaryLight)
        Spacer(modifier = Modifier.height(8.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            listOf("Carro", "Moto", "Nenhum").forEach { option ->
                FilterChip(
                    selected = vehicleType == option,
                    onClick = { vehicleType = option },
                    label = { Text(option) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = ElectricBluePrimary,
                        selectedLabelColor = BackgroundDeepBlack,
                        containerColor = GlassCard,
                        labelColor = TextPrimaryLight
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Objetivo Principal Selection
        Text("Objetivo Principal", fontWeight = FontWeight.Bold, color = TextPrimaryLight)
        Spacer(modifier = Modifier.height(8.dp))
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            listOf("Sair das dívidas", "Reserva de Emergência", "Comprar Veículo", "Investir").forEach { option ->
                FilterChip(
                    selected = primaryGoal == option,
                    onClick = { primaryGoal = option },
                    label = { Text(option) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = ElectricBluePrimary,
                        selectedLabelColor = BackgroundDeepBlack,
                        containerColor = GlassCard,
                        labelColor = TextPrimaryLight
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Dependentes
        OutlinedTextField(
            value = dependentsText,
            onValueChange = { dependentsText = it },
            label = { Text("Número de Dependentes") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Gastos Fixos & Variáveis
        OutlinedTextField(
            value = fixedExpenses,
            onValueChange = { fixedExpenses = it },
            label = { Text("Gastos Fixos Mensais (R$)") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = variableExpenses,
            onValueChange = { variableExpenses = it },
            label = { Text("Gastos Variáveis Estimados (R$)") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = debts,
            onValueChange = { debts = it },
            label = { Text("Total de Dívidas Atuais (R$)") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Save Button
        Button(
            onClick = {
                val profile = UserProfile(
                    income = income.toDoubleOrNull() ?: 0.0,
                    housing_type = housingType,
                    vehicle_type = vehicleType,
                    dependents = dependentsText.toIntOrNull() ?: 0,
                    primary_goal = primaryGoal,
                    expenses_fixed = fixedExpenses.toDoubleOrNull() ?: 0.0,
                    expenses_variable = variableExpenses.toDoubleOrNull() ?: 0.0,
                    debts = debts.toDoubleOrNull() ?: 0.0
                )
                coroutineScope.launch {
                    sessionManager.saveProfile(profile)
                    onSaveSuccess()
                }
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = ElectricBluePrimary)
        ) {
            Text("Salvar Perfil", color = BackgroundDeepBlack, fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Logout Button
        OutlinedButton(
            onClick = {
                coroutineScope.launch {
                    sessionManager.logout()
                    onLogout()
                }
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = StatusRedExpense)
        ) {
            Text("Sair da Conta (Logout)", fontWeight = FontWeight.Bold)
        }
    }
}
