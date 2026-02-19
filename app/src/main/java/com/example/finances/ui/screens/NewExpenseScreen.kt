package com.example.finances.ui.screens

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.finances.R
import com.example.finances.data.AppState
import com.example.finances.data.Expense
import com.example.finances.data.ExpenseStatus
import com.example.finances.ui.theme.AgroGreen
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewExpenseScreen(
    onBack: () -> Unit,
    onOpenCamera: () -> Unit,
    onSentTo1C: () -> Unit
) {
    var amount by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("") }
    val receiptUri = AppState.lastReceiptUri

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.receipt_registration), fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = stringResource(R.string.back))
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp),
            verticalArrangement = Arrangement.Top
        ) {
            androidx.compose.material3.OutlinedTextField(
                value = amount,
                onValueChange = { amount = it.filter { c -> c.isDigit() || c == '.' || c == ',' } },
                label = { Text(stringResource(R.string.amount_rub)) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Decimal,
                    imeAction = ImeAction.Next
                )
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = stringResource(R.string.quick_category),
                style = androidx.compose.material3.MaterialTheme.typography.labelMedium,
                color = androidx.compose.material3.MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                listOf("ГСМ", "Канцелярия", "Хоз.", "Прочее").forEach { cat ->
                    androidx.compose.material3.FilterChip(
                        selected = category == cat,
                        onClick = { category = cat },
                        label = { Text(cat) }
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            androidx.compose.material3.OutlinedTextField(
                value = category,
                onValueChange = { category = it },
                label = { Text(stringResource(R.string.category_hint)) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Done
                )
            )
            Spacer(modifier = Modifier.height(24.dp))
            OutlinedButton(
                onClick = onOpenCamera,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = androidx.compose.foundation.shape.RoundedCornerShape(8.dp)
            ) {
                Text(
                    "[${if (receiptUri != null) "1" else "0"}] ${stringResource(R.string.photograph_receipt).uppercase()}",
                    fontWeight = FontWeight.SemiBold
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            Button(
                onClick = {
                    val amountValue = amount.replace(',', '.').toDoubleOrNull() ?: 0.0
                    if (amountValue > 0 && category.isNotBlank()) {
                        val date = SimpleDateFormat("dd.MM", Locale.getDefault()).format(Date())
                        AppState.addExpense(
                            Expense(
                                id = AppState.nextExpenseId(),
                                date = date,
                                description = category,
                                amountRub = amountValue,
                                status = ExpenseStatus.UNDER_REVIEW,
                                category = category,
                                receiptImageUri = receiptUri
                            )
                        )
                        AppState.clearLastReceipt()
                        onSentTo1C()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                colors = ButtonDefaults.buttonColors(containerColor = AgroGreen),
                shape = androidx.compose.foundation.shape.RoundedCornerShape(8.dp)
            ) {
                Text(stringResource(R.string.send_to_1c).uppercase(), fontWeight = FontWeight.SemiBold)
            }
        }
    }
}
