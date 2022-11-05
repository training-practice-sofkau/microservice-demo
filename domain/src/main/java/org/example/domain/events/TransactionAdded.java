package org.example.domain.events;

import org.example.domain.value.Name;
import org.example.domain.value.TransactionId;
import org.example.generic.domain.DomainEvent;

import java.util.Date;

public class TransactionAdded extends DomainEvent {
    private final TransactionId id;
    private final Date date;
    private final Name name;

    public TransactionAdded(TransactionId id, Date date) {
        super("org.example.TransactionAdded");
        this.id = id;
        this.date = date;
        this.name = new Name("Creation");
    }

    public Date getDate() {
        return date;
    }

    public TransactionId getId() {
        return id;
    }

    public Name getName() {
        return name;
    }
}
