package by.rusakovich.publisher.generics;

import java.util.List;

public interface IEntityService<Id, RequestTO, ResponseTO> {
    ResponseTO readById(Id id);
    List<ResponseTO> readAll();
    ResponseTO create(RequestTO newEntity);
    ResponseTO update(RequestTO updatedEntity);
    void deleteById(Id id);
}
