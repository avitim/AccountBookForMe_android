package com.example.AccountBookForMe.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "expenses")
public class Expense {

    @Id
    @GeneratedValue
    Long id;

    @Column(name = "total_amount")
    Float totalAmount = null;

    @Column(name = "store_id")
    Long storeId = null;

    @Column(name = "purchased_at")
    LocalDateTime purchasedAt = null;

    @Column(name = "note", length = 140)
    String note = null;

}
