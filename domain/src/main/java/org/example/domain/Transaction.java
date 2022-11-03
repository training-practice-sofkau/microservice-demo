package org.example.domain;

import org.example.domain.value.TransactionId;
import org.example.generic.domain.Entity;

import java.util.Date;

public class Transaction extends Entity<TransactionId> {

    private Date date;//TODO: crear objeto de valor
    public Transaction(TransactionId id, Date date) {
        super(id);
        this.date = date;
    }
}
