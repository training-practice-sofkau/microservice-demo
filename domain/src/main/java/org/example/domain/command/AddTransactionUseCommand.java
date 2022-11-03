package org.example.domain.command;

import org.example.domain.value.AccountId;
import org.example.generic.domain.Command;

import java.util.Date;

public class AddTransactionUseCommand extends Command {
    private final AccountId id;
    private final Date date;

    public AddTransactionUseCommand(AccountId id, Date date) {
        this.id = id;
        this.date = date;
    }

    public AccountId getId() {
        return id;
    }

    public Date getDate() {
        return date;
    }
}
