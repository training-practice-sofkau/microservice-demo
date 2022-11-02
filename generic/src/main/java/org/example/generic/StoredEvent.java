package org.example.generic;




import java.util.Date;

public class StoredEvent {


    private String eventBody;
    private Date occurredOn;
    private String typeName;

    public StoredEvent() {
    }



    public StoredEvent(String typeName, Date occurredOn, String eventBody) {
        this.setEventBody(eventBody);
        this.setOccurredOn(occurredOn);
        this.setTypeName(typeName);
    }


    public static StoredEvent wrapEvent(DomainEvent domainEvent, EventSerializer serializer) {
        return new StoredEvent(domainEvent.getClass().getCanonicalName(),
                Date.from(domainEvent.when), serializer.serialize(domainEvent)
        );
    }


    public String getEventBody() {
        return eventBody;
    }


    public void setEventBody(String eventBody) {
        this.eventBody = eventBody;
    }


    public Date getOccurredOn() {
        return occurredOn;
    }


    public void setOccurredOn(Date occurredOn) {
        this.occurredOn = occurredOn;
    }


    public String getTypeName() {
        return typeName;
    }


    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }


    public DomainEvent deserializeEvent(EventSerializer serializer) {
        try {
            return serializer.deserialize(this.getEventBody(), Class.forName(this.getTypeName()));
        } catch (ClassNotFoundException e) {
            throw new DeserializeEventException(e.getCause());
        }
    }

}