package com.example.finances.data

import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

/**
 * Глобальное состояние приложения (для демо без бэкенда 1С).
 */
object AppState {
    private const val ADMIN_PERSONNEL = "0000"
    private const val ADMIN_PASSWORD = "AgroAdmin123"

    var currentUser: User? by mutableStateOf(null)
    private val users = mutableStateListOf<User>()
    val expenses = mutableStateListOf<Expense>()
    var lastReceiptUri: String? by mutableStateOf(null)
    var pendingReceiptUri: Uri? = null

    fun setLastReceiptUri() {
        lastReceiptUri = pendingReceiptUri?.toString()
    }

    fun clearLastReceipt() {
        lastReceiptUri = null
        pendingReceiptUri = null
    }

    fun login(personnelNumber: String, password: String): Boolean {
        if (personnelNumber.isBlank() || password.isBlank()) return false
        ensureAdminUser()

        val user = users.find { it.personnelNumber == personnelNumber && it.password == password }
        currentUser = user

        if (user != null && expenses.isEmpty()) {
            // Демонстрационные данные расходов
            expenses.addAll(
                listOf(
                    Expense(1, "12.05", "Бензин АИ-95", 2500.0, ExpenseStatus.APPROVED, "ГСМ"),
                    Expense(2, "14.05", "Канцелярия", 850.0, ExpenseStatus.UNDER_REVIEW, "Хоз.")
                )
            )
        }
        return user != null
    }

    fun logout() {
        currentUser = null
    }

    fun registerUser(
        personnelNumber: String,
        name: String,
        department: String,
        password: String,
        isAdmin: Boolean = false
    ): Boolean {
        if (personnelNumber.isBlank() || name.isBlank() || department.isBlank() || password.isBlank()) {
            return false
        }
        if (users.any { it.personnelNumber == personnelNumber }) {
            return false
        }
        users.add(
            User(
                personnelNumber = personnelNumber,
                name = name,
                department = department,
                isAdmin = isAdmin,
                password = password
            )
        )
        return true
    }

    fun allUsers(): List<User> = users

    private fun ensureAdminUser() {
        if (users.none { it.isAdmin }) {
            users.add(
                User(
                    personnelNumber = ADMIN_PERSONNEL,
                    name = "Администратор",
                    department = "Администрирование",
                    isAdmin = true,
                    password = ADMIN_PASSWORD
                )
            )
        }
    }

    fun addExpense(expense: Expense) {
        expenses.add(0, expense)
    }

    fun removeExpense(id: Long) {
        expenses.removeAll { it.id == id }
    }

    private var nextId = 3L
    fun nextExpenseId(): Long = nextId++
}
