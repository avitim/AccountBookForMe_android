package com.example.accountbookforme.model

data class ExpenseDetail(

    var expense: Expense,

    var paymentMethods: List<PaymentListItem>

)
