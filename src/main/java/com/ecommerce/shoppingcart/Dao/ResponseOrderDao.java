package com.ecommerce.shoppingcart.Dao;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class ResponseOrderDao {

    private double amount;
    private LocalDate date;
    private int invoiceNumber;
    private Long orderId;
    private String orderDescription;

    public ResponseOrderDao(double amount, LocalDate date, int invoiceNumber, Long orderId, String orderDescription) {
        this.amount = amount;
        this.date = date;
        this.invoiceNumber = invoiceNumber;
        this.orderId = orderId;
        this.orderDescription = orderDescription;
    }
}
