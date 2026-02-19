package com.example.finances.data

/**
 * Модели данных для приложения Агро-Расходы.
 */

data class User(
    val personnelNumber: String,
    val name: String,
    val department: String,
    val isAdmin: Boolean = false,
    val password: String
)

enum class ExpenseStatus(val displayName: String) {
    APPROVED("ОДОБРЕНО"),
    UNDER_REVIEW("НА ПРОВЕРКЕ"),
    REJECTED("ОТКЛОНЕНО")
}

data class Expense(
    val id: Long,
    val date: String,
    val description: String,
    val amountRub: Double,
    val status: ExpenseStatus,
    val category: String,
    val receiptImageUri: String? = null
)
