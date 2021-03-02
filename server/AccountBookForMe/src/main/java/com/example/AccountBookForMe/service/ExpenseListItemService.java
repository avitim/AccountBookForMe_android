package com.example.AccountBookForMe.service;

import com.example.AccountBookForMe.entity.ExpenseListItem;
import com.example.AccountBookForMe.repository.ExpenseListItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class ExpenseListItemService {

    @Autowired
    private ExpenseListItemRepository expenseListItemRepository;

    /**
     * Expenses画面に表示する値のセットをリストで返す
     * @return
     */
    public List<ExpenseListItem> getExpenseList() {

        List<ExpenseListItem> list = expenseListItemRepository.getExpenseList();
        return formatDate(list);
    }

    /**
     * リストごと日付のフォーマット変換して返す
     */
    private List<ExpenseListItem> formatDate(List<ExpenseListItem> list) {

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("E, d");
        list.forEach(item -> {
            item.setPurchasedAt(dtf.format(item.getFullPurchasedAt()));
        });

        return list;
    }
}
