package com.example.account_book_for_me.repository

import com.example.account_book_for_me.entity.Expense
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ExpenseRepository : JpaRepository<Expense, Long> {
}