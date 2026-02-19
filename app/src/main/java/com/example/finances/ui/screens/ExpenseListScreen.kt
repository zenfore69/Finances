package com.example.finances.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.finances.R
import com.example.finances.data.AppState
import com.example.finances.data.Expense
import com.example.finances.data.ExpenseStatus

enum class ExpenseFilter { ALL, APPROVED, UNDER_REVIEW }
enum class ExpenseSort { DATE_DESC, DATE_ASC, AMOUNT_DESC, AMOUNT_ASC }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpenseListScreen(onBack: () -> Unit, onExpenseClick: (Long) -> Unit) {
    val expenses = AppState.expenses
    var searchQuery by remember { mutableStateOf("") }
    var filter by remember { mutableStateOf(ExpenseFilter.ALL) }
    var sort by remember { mutableStateOf(ExpenseSort.DATE_DESC) }

    val filteredAndSorted by remember {
        derivedStateOf {
            var list = expenses.filter { expense ->
                val matchesFilter = when (filter) {
                    ExpenseFilter.ALL -> true
                    ExpenseFilter.APPROVED -> expense.status == ExpenseStatus.APPROVED
                    ExpenseFilter.UNDER_REVIEW -> expense.status == ExpenseStatus.UNDER_REVIEW
                }
                val matchesSearch = searchQuery.isBlank() ||
                    expense.description.contains(searchQuery, ignoreCase = true) ||
                    expense.category.contains(searchQuery, ignoreCase = true)
                matchesFilter && matchesSearch
            }
            list = when (sort) {
                ExpenseSort.DATE_DESC -> list.sortedByDescending { parseDate(it.date) }
                ExpenseSort.DATE_ASC -> list.sortedBy { parseDate(it.date) }
                ExpenseSort.AMOUNT_DESC -> list.sortedByDescending { it.amountRub }
                ExpenseSort.AMOUNT_ASC -> list.sortedBy { it.amountRub }
            }
            list
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.my_expenses), fontWeight = FontWeight.Bold) },
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
                .padding(horizontal = 16.dp)
        ) {
            androidx.compose.material3.OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                placeholder = { Text(stringResource(R.string.search_hint)) },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Search
                )
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FilterChip(
                    selected = filter == ExpenseFilter.ALL,
                    onClick = { filter = ExpenseFilter.ALL },
                    label = { Text(stringResource(R.string.filter_all)) }
                )
                FilterChip(
                    selected = filter == ExpenseFilter.APPROVED,
                    onClick = { filter = ExpenseFilter.APPROVED },
                    label = { Text(stringResource(R.string.approved)) }
                )
                FilterChip(
                    selected = filter == ExpenseFilter.UNDER_REVIEW,
                    onClick = { filter = ExpenseFilter.UNDER_REVIEW },
                    label = { Text(stringResource(R.string.under_review)) }
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(stringResource(R.string.sort_by) + " ", fontSize = 14.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                FilterChip(
                    selected = sort == ExpenseSort.DATE_DESC || sort == ExpenseSort.DATE_ASC,
                    onClick = { sort = if (sort == ExpenseSort.DATE_DESC) ExpenseSort.DATE_ASC else ExpenseSort.DATE_DESC },
                    label = { Text(if (sort == ExpenseSort.DATE_DESC) stringResource(R.string.sort_date_new) else stringResource(R.string.sort_date_old)) }
                )
                Spacer(modifier = Modifier.padding(4.dp))
                FilterChip(
                    selected = sort == ExpenseSort.AMOUNT_DESC || sort == ExpenseSort.AMOUNT_ASC,
                    onClick = { sort = if (sort == ExpenseSort.AMOUNT_DESC) ExpenseSort.AMOUNT_ASC else ExpenseSort.AMOUNT_DESC },
                    label = { Text(if (sort == ExpenseSort.AMOUNT_DESC) stringResource(R.string.sort_amount_high) else stringResource(R.string.sort_amount_low)) }
                )
            }
            if (filteredAndSorted.isEmpty()) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = stringResource(R.string.no_expenses),
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = stringResource(R.string.no_expenses_hint),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(filteredAndSorted) { expense ->
                        ExpenseItem(expense = expense, onClick = { onExpenseClick(expense.id) })
                    }
                }
            }
        }
    }
}

private fun parseDate(s: String): Int {
    val parts = s.split(".").mapNotNull { it.toIntOrNull() }
    if (parts.size >= 2) return parts[0] + parts[1] * 100
    return 0
}

@Composable
private fun ExpenseItem(expense: Expense, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "${expense.date} | ${expense.description}",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = "${stringResource(R.string.amount)}: ${formatRub(expense.amountRub)}",
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = "${stringResource(R.string.status)}: [${expense.status.displayName}]",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = when (expense.status) {
                    ExpenseStatus.APPROVED -> MaterialTheme.colorScheme.primary
                    ExpenseStatus.UNDER_REVIEW -> MaterialTheme.colorScheme.tertiary
                    else -> MaterialTheme.colorScheme.error
                }
            )
        }
    }
}

private fun formatRub(value: Double): String =
    "%,.0f руб.".format(value).replace(',', ' ')
