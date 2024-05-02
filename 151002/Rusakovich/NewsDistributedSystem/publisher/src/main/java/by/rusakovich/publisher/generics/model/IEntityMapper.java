package by.rusakovich.publisher.generics.model;

public interface IEntityMapper<Id, Entity extends IEntity<Id>, Request, Response> {
    Entity mapToEntity(Request request);
    Response mapToResponse(Entity entity);
}
