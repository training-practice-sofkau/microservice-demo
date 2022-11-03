package org.example.generic.domain;

import com.google.gson.JsonDeserializer;

import java.io.Serializable;

public interface ValueObject<T> extends Serializable {
    T value();
}
