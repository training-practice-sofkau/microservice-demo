package org.example.generic;

public interface EventSerializer {
     <T extends DomainEvent> T deserialize(String aSerialization, final Class<?> aType);

     String serialize(DomainEvent object);

}
