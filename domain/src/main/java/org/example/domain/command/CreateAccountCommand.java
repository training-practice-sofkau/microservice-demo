package org.example.domain.command;

import org.example.domain.value.AccountId;
import org.example.domain.value.Name;
import org.example.domain.value.UserId;
import org.example.generic.domain.Command;

public class CreateAccountCommand extends Command {

    private final AccountId id;
    private final UserId userId;
    private final Name name;

    public CreateAccountCommand(AccountId id, UserId userId, Name name) {
        this.id = id;
        this.userId = userId;
        this.name = name;
    }

    public UserId getUserId() {
        return userId;
    }

    public Name getName() {
        return name;
    }

    public AccountId getId() {
        return id;
    }
}
