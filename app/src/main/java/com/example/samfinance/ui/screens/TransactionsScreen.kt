package com.example.samfinance.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.samfinance.data.SessionManager
import com.example.samfinance.network.TransactionItem
import com.example.samfinance.ui.theme.*
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionsScreen() {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val sessionManager = remember { SessionManager(context) }
    val transactionsState by sessionManager.transactions.collectAsState(initial = emptyList())

    var selectedFilter by remember { mutableStateOf("Todas") }
    var showAddBottomSheet by remember { mutableStateOf(false) }

    val filteredTransactions = when (selectedFilter) {
        "Receitas" -> transactionsState.filter { it.isIncome }
        "Despesas" -> transactionsState.filter { !it.isIncome }
        else -> transactionsState
    }

    Scaffold(
        containerColor = BackgroundDeepBlack,
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showAddBottomSheet = true },
                containerColor = ElectricBluePrimary,
                contentColor = BackgroundDeepBlack,
                shape = CircleShape
            ) {
                Icon(Icons.Default.Add, contentDescription = "Adicionar Transação")
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            Text(
                text = "Minhas Transações",
                style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                color = TextPrimaryLight
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Filter Chips
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                listOf("Todas", "Receitas", "Despesas").forEach { filter ->
                    val isSelected = selectedFilter == filter
                    FilterChip(
                        selected = isSelected,
                        onClick = { selectedFilter = filter },
                        label = { Text(filter) },
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

            if (filteredTransactions.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Nenhuma transação encontrada.\nToque no botão + para adicionar.",
                        color = TextSecondaryMuted,
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    items(filteredTransactions) { transaction ->
                        TransactionRow(transaction)
                    }
                }
            }
        }
    }

    // Modal Bottom Sheet to add transaction
    if (showAddBottomSheet) {
        AddTransactionBottomSheet(
            onDismiss = { showAddBottomSheet = false },
            onAddTransaction = { title, amount, category, isIncome ->
                val newItem = TransactionItem(
                    title = title,
                    amount = amount,
                    category = category,
                    isIncome = isIncome
                )
                val newList = transactionsState + newItem
                coroutineScope.launch {
                    sessionManager.saveTransactions(newList)
                    showAddBottomSheet = false
                }
            }
        )
    }
}

@Composable
fun TransactionRow(item: TransactionItem) {
    GlassCard(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(text = item.title, fontWeight = FontWeight.Bold, color = TextPrimaryLight)
                Text(text = "${item.category} • ${item.date}", fontSize = 12.sp, color = TextSecondaryMuted)
            }
            Text(
                text = "${if (item.isIncome) "+" else "-"} R$ %.2f".format(item.amount),
                color = if (item.isIncome) StatusGreenIncome else StatusRedExpense,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTransactionBottomSheet(
    onDismiss: () -> Unit,
    onAddTransaction: (title: String, amount: Double, category: String, isIncome: Boolean) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var amountText by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("Geral") }
    var isIncome by remember { mutableStateOf(false) }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = BackgroundCharcoal
    ) {
        Column(
            modifier = Modifier
                .padding(24.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = "Nova Transação",
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                color = TextPrimaryLight
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Type selector (Receita / Despesa)
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Button(
                    onClick = { isIncome = false },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (!isIncome) StatusRedExpense else GlassCard
                    ),
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Despesa")
                }
                Button(
                    onClick = { isIncome = true },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isIncome) StatusGreenIncome else GlassCard
                    ),
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Receita")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Descrição / Título") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = amountText,
                onValueChange = { amountText = it },
                label = { Text("Valor (R$)") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = category,
                onValueChange = { category = it },
                label = { Text("Categoria (ex: Alimentação, Salário, Moto)") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    val amount = amountText.toDoubleOrNull() ?: 0.0
                    if (title.isNotBlank() && amount > 0) {
                        onAddTransaction(title, amount, category, isIncome)
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = ElectricBluePrimary)
            ) {
                Text("Adicionar", color = BackgroundDeepBlack, fontWeight = FontWeight.Bold)
            }
        }
    }
}
