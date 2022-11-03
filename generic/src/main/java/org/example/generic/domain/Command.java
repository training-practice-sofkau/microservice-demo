package org.example.generic.domain;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;


public abstract class Command implements Serializable {

    public final Instant when;
    private final String uuid;


    public Command() {
        this.uuid = UUID.randomUUID().toString();
        this.when = Instant.now();
    }


    public Instant when() {
        return when;
    }


    public String uuid() {
        return uuid;
    }
}