package com.example.AccountBookForMe.controller;

import com.example.AccountBookForMe.entity.Expense;
import com.example.AccountBookForMe.service.ExpenseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/expenses")
public class ExpenseController {

    @Autowired
    private ExpenseService expenseService;

    @GetMapping("")
    List<Expense> getAll() {
        return expenseService.findAll();
    }

    @PostMapping("")
    Expense save(@RequestBody Expense expense) {
        return expenseService.save(expense);
    }

    @PutMapping("/{id}")
    Expense update(@PathVariable Long id, @RequestBody Expense expense) {
        expense.setId(id);
        return expenseService.update(expense);
    }

    @DeleteMapping("/{id}")
    void delete(@PathVariable Long id) {
        expenseService.delete(id);
    }
}
