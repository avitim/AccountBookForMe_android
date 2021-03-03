package com.example.AccountBookForMe.entity;

import lombok.Data;

import java.util.ArrayList;

@Data
public class ExpenseForm {

    Expense expense;

    ArrayList<Long> paymentMethods;
}
