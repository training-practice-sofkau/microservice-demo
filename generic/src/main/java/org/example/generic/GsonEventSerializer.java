package org.example.generic;

import com.google.gson.Gson;
import org.example.generic.business.EventSerializer;
import org.example.generic.domain.DomainEvent;


public class GsonEventSerializer extends AbstractSerializer implements EventSerializer {
    @Override
    public <T extends DomainEvent> T deserialize(String aSerialization, Class<?> aType) {
        return (T) new Gson().fromJson(aSerialization, aType);
    }

    @Override
    public String serialize(DomainEvent object) {
        return new Gson().toJson(object);
    }
}