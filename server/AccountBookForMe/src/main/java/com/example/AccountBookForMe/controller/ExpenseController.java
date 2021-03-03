package com.example.AccountBookForMe.controller;

import com.example.AccountBookForMe.entity.Expense;
import com.example.AccountBookForMe.entity.ExpenseForm;
import com.example.AccountBookForMe.service.ExpensePaymentMethodService;
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

    @Autowired
    private ExpensePaymentMethodService expensePaymentMethodService;

    @GetMapping("")
    List<Expense> getAll() {
        return expenseService.findAll();
    }

    /**
     * 支出の新規作成
     * @param expenseForm
     * @return
     */
    @PostMapping("")
    Expense save(@RequestBody ExpenseForm expenseForm) {

        Expense e = expenseService.save(expenseForm.getExpense());

        // 関連するexpenses_payment_methodsも新規作成する
        expensePaymentMethodService.create(expenseForm);

        return e;
    }

    /**
     * 支出の更新
     * @param id
     * @param expenseForm
     * @return
     */
    @PutMapping("/{id}")
    Expense update(@PathVariable Long id, @RequestBody ExpenseForm expenseForm) {

        Expense expense = expenseForm.getExpense();
        expense.setId(id);
        Expense e = expenseService.update(expense);

        // 関連するexpenses_payment_methodsも更新する
        expensePaymentMethodService.update(expenseForm);

        return e;
    }

    /**
     * 支出の削除
     * @param id
     */
    @DeleteMapping("/{id}")
    void delete(@PathVariable Long id) {

        expenseService.delete(id);

        // 関連するexpenses_payment_methodsも削除する
        expensePaymentMethodService.delete(id);
    }
}
