package org.example.domain;

import org.example.domain.events.AccountCreated;
import org.example.domain.events.TransactionAdded;
import org.example.generic.domain.EventChange;

import java.util.HashMap;

public class AccountEventChange extends EventChange {
    public AccountEventChange(Account account) {
        apply((AccountCreated event) -> {
            account.name = event.getName();
            account.userId = event.getUserId();
            account.transactions = new HashMap<>();
        });

        apply((TransactionAdded event) -> {
            account.transactions.put(event.getId(),
                    new Transaction(event.getId(), event.getDate())
            );
        });
    }
}
