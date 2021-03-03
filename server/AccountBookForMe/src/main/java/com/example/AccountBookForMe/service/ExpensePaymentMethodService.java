package com.example.AccountBookForMe.service;

import com.example.AccountBookForMe.entity.Expense;
import com.example.AccountBookForMe.entity.ExpenseForm;
import com.example.AccountBookForMe.entity.ExpensePaymentMethod;
import com.example.AccountBookForMe.repository.ExpensePaymentMethodRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ExpensePaymentMethodService {

    @Autowired
    private ExpensePaymentMethodRepository expensePaymentMethodRepository;

    /**
     * 支出を作成するときに同時に作成する
     * @param expenseForm: 支出詳細と支払方法IDのリストのセット
     */
    public void create(ExpenseForm expenseForm) {

        expenseForm.getPaymentMethods().forEach(methodId -> {
            ExpensePaymentMethod epm = new ExpensePaymentMethod();
            epm.setExpenseId(expenseForm.getExpense().getId());
            epm.setPaymentMethodId(methodId);
            // TODO: 支払方法が複数選択できるようになったら以下の処理を変える必要あり
            epm.setSubAmount(expenseForm.getExpense().getTotalAmount());
            expensePaymentMethodRepository.save(epm);
        });
    }

    /**
     * 支出を更新するときに同時に更新する
     * @param expenseForm: 支出詳細と支払方法IDのリストのセット
     */
    public void update(ExpenseForm expenseForm) {

        // 該当の支出に紐付いた既存のレコードを削除する
        delete(expenseForm.getExpense().getId());

        // あらためて新規作成する
        create(expenseForm);
    }

    /**
     * 支出を削除するときに同時に削除する
     * @param expenseId: 支出詳細ID
     */
    public void delete(Long expenseId) {

        List<ExpensePaymentMethod> listToDelete = expensePaymentMethodRepository.findByExpenseId(expenseId);
        listToDelete.forEach(item -> expensePaymentMethodRepository.delete(item));
    }
}
