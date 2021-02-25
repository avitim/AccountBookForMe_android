package com.example.account_book_for_me.service

import com.example.account_book_for_me.entity.Expense
import com.example.account_book_for_me.repository.ExpenseRepository
import org.springframework.stereotype.Service

@Service
class ExpenseService(private val expenseRepository: ExpenseRepository) {

    fun findAll(): List<Expense> = expenseRepository.findAll()

    fun findById(id: Long) = expenseRepository.findById(id)

    fun save(expense: Expense) = expenseRepository.save(expense)

    fun delete(id: Long) = expenseRepository.deleteById(id)
}