package com.example.AccountBookForMe.entity;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * 支出詳細画面で表示する用
 */
@Data
public class ExpenseDetail {

    Expense expense;

    Map<Long, Float> paymentMethods;

}
