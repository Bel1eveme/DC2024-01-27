package by.rusakovich.publisher.generics.model;

public interface IEntity<Id> {
    Id getId();
    void setId(Id newId);
}
