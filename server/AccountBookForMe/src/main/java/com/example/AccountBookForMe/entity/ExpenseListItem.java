package com.example.AccountBookForMe.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDateTime;

/**
 * Expenses画面でリスト表示する用
 */
@Data
@Entity
public class ExpenseListItem {

    @Id
    Long expenseId;

    LocalDateTime purchasedAt = null;

    Float price = 0F;

    String method;

    String store = "";

}
