package com.example.samfinance.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.samfinance.data.SessionManager
import com.example.samfinance.network.TransactionItem
import com.example.samfinance.network.UserProfile
import com.example.samfinance.ui.theme.*

@Composable
fun DashboardScreen(
    onNavigateToChat: () -> Unit,
    onNavigateToProfile: () -> Unit
) {
    val context = LocalContext.current
    val sessionManager = remember { SessionManager(context) }
    
    val profileState by sessionManager.userProfile.collectAsState(initial = null)
    val transactionsState by sessionManager.transactions.collectAsState(initial = emptyList())

    val profile = profileState ?: UserProfile()

    val totalIncome = profile.income + transactionsState.filter { it.isIncome }.sumOf { it.amount }
    val totalExpenses = profile.expenses_fixed + profile.expenses_variable + transactionsState.filter { !it.isIncome }.sumOf { it.amount }
    val netBalance = totalIncome - totalExpenses - profile.debts

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundDeepBlack)
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        // Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Olá, bem-vindo!",
                    style = MaterialTheme.typography.titleMedium,
                    color = TextSecondaryMuted
                )
                Text(
                    text = "SamFinance Dashboard",
                    style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                    color = TextPrimaryLight
                )
            }

            // Health Badge
            val healthText = profile.classify()
            val healthColor = when (healthText) {
                "Endividado" -> StatusRedExpense
                "Pronto para investir" -> StatusGreenIncome
                else -> StatusWarningOrange
            }

            Surface(
                color = healthColor.copy(alpha = 0.2f),
                border = androidx.compose.foundation.BorderStroke(1.dp, healthColor),
                shape = RoundedCornerShape(20.dp)
            ) {
                Text(
                    text = healthText,
                    color = healthColor,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Main Balance Glass Card
        GlassCard(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text("Saldo Estimado", color = TextSecondaryMuted, fontSize = 14.sp)
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "R$ %.2f".format(netBalance),
                    color = if (netBalance >= 0) ElectricBluePrimary else StatusRedExpense,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.ExtraBold
                )

                Spacer(modifier = Modifier.height(16.dp))
                Divider(color = GlassBorder)
                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text("Renda Total", color = TextSecondaryMuted, fontSize = 12.sp)
                        Text(
                            text = "R$ %.2f".format(totalIncome),
                            color = StatusGreenIncome,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                    Column {
                        Text("Gastos Estimados", color = TextSecondaryMuted, fontSize = 12.sp)
                        Text(
                            text = "R$ %.2f".format(totalExpenses),
                            color = StatusRedExpense,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                    Column {
                        Text("Dívidas", color = TextSecondaryMuted, fontSize = 12.sp)
                        Text(
                            text = "R$ %.2f".format(profile.debts),
                            color = StatusWarningOrange,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Expense Progress / Budget Bar
        GlassCard(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Uso da Renda", fontWeight = FontWeight.Bold, color = TextPrimaryLight)
                Spacer(modifier = Modifier.height(8.dp))
                
                val progress = if (totalIncome > 0) (totalExpenses / totalIncome).coerceIn(0.0, 1.0).toFloat() else 0f
                LinearProgressIndicator(
                    progress = progress,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(10.dp),
                    color = if (progress > 0.8f) StatusRedExpense else ElectricBluePrimary,
                    trackColor = BackgroundCharcoal
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "%.0f%% da sua renda está comprometida com gastos fixos/variáveis.".format(progress * 100),
                    fontSize = 12.sp,
                    color = TextSecondaryMuted
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Smart Alerts Section
        Text(
            text = "Alertas Inteligentes do Sam",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            color = TextPrimaryLight
        )
        Spacer(modifier = Modifier.height(10.dp))

        if (profile.vehicle_type != "Nenhum") {
            GlassCard(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Warning,
                        contentDescription = "Alerta",
                        tint = StatusWarningOrange,
                        modifier = Modifier.size(28.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text("Atenção com seu veículo (${profile.vehicle_type})", fontWeight = FontWeight.Bold, color = TextPrimaryLight)
                        Text("Lembre-se de reservar valores para IPVA, licenciamento e manutenção de seu veículo.", fontSize = 12.sp, color = TextSecondaryMuted)
                    }
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
        }

        if (profile.debts > 0) {
            GlassCard(
                modifier = Modifier.fillMaxWidth(),
                onClick = onNavigateToChat
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Warning,
                        contentDescription = "Dívida",
                        tint = StatusRedExpense,
                        modifier = Modifier.size(28.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text("Foco: Quitação de Dívidas", fontWeight = FontWeight.Bold, color = TextPrimaryLight)
                        Text("Sua prioridade deve ser renegociar juros altos. Toque para conversar com o SamFinance.", fontSize = 12.sp, color = ElectricBluePrimary)
                    }
                }
            }
        } else if (profile.income <= 0) {
            GlassCard(
                modifier = Modifier.fillMaxWidth(),
                onClick = onNavigateToProfile
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Preencha seu Perfil Financeiro", fontWeight = FontWeight.Bold, color = ElectricBluePrimary)
                    Text("Adicione sua renda e gastos para receber diagnósticos personalizados do SamFinance.", fontSize = 12.sp, color = TextSecondaryMuted)
                }
            }
        }
    }
}
