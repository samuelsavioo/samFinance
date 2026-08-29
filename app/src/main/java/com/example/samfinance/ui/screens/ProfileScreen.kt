package com.example.samfinance.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ProfileScreen(
    onSaveSuccess: () -> Unit
) {
    var income by remember { mutableStateOf("") }
    var housingType by remember { mutableStateOf("") }
    var fixedExpenses by remember { mutableStateOf("") }
    var variableExpenses by remember { mutableStateOf("") }

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
        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = { onSaveSuccess() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Salvar Perfil")
        }
    }
}
