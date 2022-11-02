package org.example.domain.events;

import org.example.domain.value.Name;
import org.example.domain.value.UserId;
import org.example.generic.DomainEvent;

public class AccountCreated extends DomainEvent {
    private final UserId userId;
    private final Name name;

    public AccountCreated(UserId userId, Name name) {
        super("org.example.AccountCreated");
        this.userId = userId;
        this.name = name;
    }

    public Name getName() {
        return name;
    }

    public UserId getUserId() {
        return userId;
    }
}
