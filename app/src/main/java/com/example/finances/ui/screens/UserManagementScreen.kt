package com.example.finances.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.finances.R
import com.example.finances.data.AppState
import com.example.finances.data.User

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserManagementScreen(onBack: () -> Unit) {
    val current = AppState.currentUser
    if (current?.isAdmin != true) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(stringResource(R.string.user_management), fontWeight = FontWeight.Bold) },
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
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = stringResource(R.string.admin_only),
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
        return
    }

    var personnelNumber by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var department by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isAdmin by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf<String?>(null) }

    val users = AppState.allUsers()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.user_management), fontWeight = FontWeight.Bold) },
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
                .padding(16.dp)
        ) {
            Text(
                text = stringResource(R.string.create_user),
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = personnelNumber,
                onValueChange = { personnelNumber = it; error = null },
                label = { Text(stringResource(R.string.personnel_number)) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = name,
                onValueChange = { name = it; error = null },
                label = { Text(stringResource(R.string.full_name)) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = department,
                onValueChange = { department = it; error = null },
                label = { Text(stringResource(R.string.department)) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = password,
                onValueChange = { password = it; error = null },
                label = { Text(stringResource(R.string.password)) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            Row(
                verticalAlignment = androidx.compose.ui.Alignment.CenterVertically,
                modifier = Modifier.padding(top = 4.dp)
            ) {
                Checkbox(checked = isAdmin, onCheckedChange = { isAdmin = it })
                Text(text = stringResource(R.string.is_admin))
            }
            error?.let {
                Text(
                    text = it,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            Button(
                onClick = {
                    val ok = AppState.registerUser(
                        personnelNumber = personnelNumber.trim(),
                        name = name.trim(),
                        department = department.trim(),
                        password = password,
                        isAdmin = isAdmin
                    )
                    if (ok) {
                        personnelNumber = ""
                        name = ""
                        department = ""
                        password = ""
                        isAdmin = false
                        error = null
                    } else {
                        error = "Не удалось создать пользователя. Проверьте уникальность табельного номера и заполнение полей."
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(stringResource(R.string.create_user).uppercase(), fontWeight = FontWeight.SemiBold)
            }

            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = stringResource(R.string.user_list),
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(8.dp))

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(users) { user ->
                    UserItem(user)
                }
            }
        }
    }
}

@Composable
private fun UserItem(user: User) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(
                text = "${user.name} (${user.personnelNumber})",
                fontWeight = FontWeight.Medium
            )
            Text(
                text = user.department,
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            if (user.isAdmin) {
                Text(
                    text = stringResource(R.string.admin_label),
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

