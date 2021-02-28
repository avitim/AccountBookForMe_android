package com.example.AccountBookForMe.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "expenses_payment_methods")
public class ExpensePaymentMethod {

    @Id
    @GeneratedValue
    Long id;

    @Column(name = "payment_method_id")
    Long paymentMethodId = null;

    @Column(name = "expense_id")
    Long expenseId = null;

}
