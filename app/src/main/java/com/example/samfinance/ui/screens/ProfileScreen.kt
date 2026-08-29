package com.example.samfinance.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.samfinance.data.SessionManager
import com.example.samfinance.network.UserProfile
import kotlinx.coroutines.launch

@Composable
fun ProfileScreen(
    onSaveSuccess: () -> Unit
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val sessionManager = remember { SessionManager(context) }
    val savedProfile by sessionManager.userProfile.collectAsState(initial = null)

    var income by remember { mutableStateOf("") }
    var housingType by remember { mutableStateOf("") }
    var fixedExpenses by remember { mutableStateOf("") }
    var variableExpenses by remember { mutableStateOf("") }
    var debts by remember { mutableStateOf("") }
    var hasVehicle by remember { mutableStateOf(false) }

    LaunchedEffect(savedProfile) {
        savedProfile?.let {
            income = it.income.toString()
            housingType = it.housing_type
            fixedExpenses = it.expenses_fixed.toString()
            variableExpenses = it.expenses_variable.toString()
            debts = it.debts.toString()
            hasVehicle = it.has_vehicle
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text(text = "Perfil Financeiro", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = income,
            onValueChange = { income = it },
            label = { Text("Renda Mensal") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = housingType,
            onValueChange = { housingType = it },
            label = { Text("Tipo de Moradia (Aluguel, Própria, etc)") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = fixedExpenses,
            onValueChange = { fixedExpenses = it },
            label = { Text("Gastos Fixos Mensais") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = variableExpenses,
            onValueChange = { variableExpenses = it },
            label = { Text("Gastos Variáveis Estimados") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = debts,
            onValueChange = { debts = it },
            label = { Text("Total de Dívidas Atuais") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        Row(verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
            Checkbox(checked = hasVehicle, onCheckedChange = { hasVehicle = it })
            Text("Possui Veículo (Carro/Moto)")
        }
        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                val profile = UserProfile(
                    user_id = 1,
                    income = income.toDoubleOrNull() ?: 0.0,
                    housing_type = housingType,
                    expenses_fixed = fixedExpenses.toDoubleOrNull() ?: 0.0,
                    expenses_variable = variableExpenses.toDoubleOrNull() ?: 0.0,
                    debts = debts.toDoubleOrNull() ?: 0.0,
                    has_vehicle = hasVehicle
                )
                coroutineScope.launch {
                    sessionManager.saveProfile(profile)
                    onSaveSuccess()
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Salvar Perfil")
        }
    }
}
