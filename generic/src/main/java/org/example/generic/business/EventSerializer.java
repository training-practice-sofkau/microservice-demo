package org.example.generic.business;

import org.example.generic.domain.DomainEvent;

public interface EventSerializer {
     <T extends DomainEvent> T deserialize(String aSerialization, final Class<?> aType);

     String serialize(DomainEvent object);

}
