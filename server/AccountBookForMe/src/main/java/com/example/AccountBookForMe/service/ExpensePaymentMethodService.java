package com.example.AccountBookForMe.service;

import com.example.AccountBookForMe.entity.ExpenseDetail;
import com.example.AccountBookForMe.entity.ExpensePaymentMethod;
import com.example.AccountBookForMe.repository.ExpensePaymentMethodRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ExpensePaymentMethodService {

    @Autowired
    private ExpensePaymentMethodRepository expensePaymentMethodRepository;

    /**
     * 支出IDからその支出に紐付いた支払方法IDと小計のリストを返す
     * @param expenseId
     * @return
     */
    public Map<Long, Float> getByExpenseId(Long expenseId) {

        Map<Long, Float> paymentList = new HashMap<>();

        List<ExpensePaymentMethod> expensePaymentMethods = expensePaymentMethodRepository.findByExpenseId(expenseId);
        expensePaymentMethods.forEach(epm -> {
            paymentList.put(epm.getPaymentMethodId(), epm.getSubAmount());
        });

        return paymentList;
    }

    /**
     * 支出を作成するときに同時に作成する
     * @param expenseDetail: 支出詳細と支払方法IDのリストのセット
     */
    public void create(ExpenseDetail expenseDetail) {

        expenseDetail.getPaymentMethods().forEach((methodId, subAmount) -> {
            ExpensePaymentMethod epm = new ExpensePaymentMethod();
            epm.setExpenseId(expenseDetail.getExpense().getId());
            epm.setPaymentMethodId(methodId);
            epm.setSubAmount(subAmount);
            expensePaymentMethodRepository.save(epm);
        });
    }

    /**
     * 支出を更新するときに同時に更新する
     * @param expenseDetail: 支出詳細と支払方法IDのリストのセット
     */
    public void update(ExpenseDetail expenseDetail) {

        // 該当の支出に紐付いた既存のレコードを削除する
        delete(expenseDetail.getExpense().getId());

        // あらためて新規作成する
        create(expenseDetail);
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
