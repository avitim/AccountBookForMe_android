package com.example.AccountBookForMe.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "payment_methods")
public class PaymentMethod {

    @Id
    @GeneratedValue
    Long id;

    @Column(name = "name", length = 30)
    String name = null;

    @Column(name = "color_id")
    Long colorId = null;

    @Column(name = "icon_id")
    Long iconId = null;

}
