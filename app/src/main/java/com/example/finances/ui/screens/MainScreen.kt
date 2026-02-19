package com.example.finances.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Settings
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.finances.R
import com.example.finances.data.AppState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    onNewExpense: () -> Unit,
    onReportHistory: () -> Unit,
    onStatistics: () -> Unit,
    onSettings: () -> Unit
) {
    val user = AppState.currentUser ?: return

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.app_title), fontWeight = FontWeight.Bold) },
                actions = {
                    IconButton(onClick = onSettings) {
                        Icon(Icons.Default.Settings, contentDescription = stringResource(R.string.settings))
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp)
        ) {
            Text(
            text = "${stringResource(R.string.employee)}: ${user.name}",
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium
        )
        Text(
            text = user.department,
            fontSize = 16.sp,
            color = androidx.compose.material3.MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(32.dp))
        Button(
            onClick = onNewExpense,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            colors = ButtonDefaults.buttonColors(containerColor = androidx.compose.material3.MaterialTheme.colorScheme.primary),
            shape = androidx.compose.foundation.shape.RoundedCornerShape(8.dp)
        ) {
            Icon(Icons.Default.Add, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text(stringResource(R.string.new_expense).uppercase(), fontWeight = FontWeight.SemiBold)
        }
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedButton(
            onClick = onReportHistory,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape = androidx.compose.foundation.shape.RoundedCornerShape(8.dp)
        ) {
            Text(stringResource(R.string.report_history).uppercase(), fontWeight = FontWeight.SemiBold)
        }
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedButton(
                onClick = onStatistics,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = androidx.compose.foundation.shape.RoundedCornerShape(8.dp)
            ) {
                Text(stringResource(R.string.statistics).uppercase(), fontWeight = FontWeight.SemiBold)
            }
        }
    }
}
