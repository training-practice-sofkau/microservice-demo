package org.example.domain.value;

import org.example.generic.ValueObject;

import java.util.Objects;

public class Name implements ValueObject<String> {
    private final String value;

    public Name(String value) {
        this.value = Objects.requireNonNull(value);
        if(this.value.isEmpty()){
            throw new IllegalArgumentException("The name is empty");
        }

        if(this.value.length() > 300){
            throw new IllegalArgumentException("The name is greater than 300");
        }
    }

    @Override
    public String value() {
        return value;
    }
}
