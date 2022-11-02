package org.example.domain.events;

import org.example.domain.value.TransactionId;
import org.example.generic.DomainEvent;

import java.util.Date;

public class TransactionAdded extends DomainEvent {
    private final TransactionId id;
    private final Date date;

    public TransactionAdded(TransactionId id, Date date) {
        super("org.example.TransactionAdded");
        this.id = id;
        this.date = date;
    }

    public Date getDate() {
        return date;
    }

    public TransactionId getId() {
        return id;
    }
}
