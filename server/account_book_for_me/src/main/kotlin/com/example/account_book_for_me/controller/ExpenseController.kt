package com.example.account_book_for_me.controller

import com.example.account_book_for_me.entity.Expense
import com.example.account_book_for_me.service.ExpenseService
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
@RequestMapping("/api/v1/expenses")
class ExpenseController(private val expenseService: ExpenseService) {

    @GetMapping("")
    fun findAll(): List<Expense> {
        return expenseService.findAll()
    }

    @PostMapping("")
    fun create(@RequestBody expense: Expense): Expense {
        expenseService.save(expense)
        return expense
    }

    @GetMapping("{id}")
    fun findById(@PathVariable id: Long): Optional<Expense> {
        return expenseService.findById(id)
    }

    @PutMapping("{id}")
    fun update(@PathVariable id: Long, @RequestBody expense: Expense): Expense {
        expenseService.save(expense.copy(id = id))
        return expense.copy(id = id)
    }

    @DeleteMapping("{id}")
    fun delete(@PathVariable id: Long): String {
        expenseService.delete(id)
        return "Completed to delete"
    }
}