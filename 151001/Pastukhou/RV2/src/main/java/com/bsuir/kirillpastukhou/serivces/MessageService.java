package com.bsuir.kirillpastukhou.serivces;

import com.bsuir.kirillpastukhou.domain.entity.ValidationMarker;
import com.bsuir.kirillpastukhou.domain.request.MessageRequestTo;
import com.bsuir.kirillpastukhou.domain.response.MessageResponseTo;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;

import java.util.List;

public interface MessageService {
    @Validated(ValidationMarker.OnCreate.class)
    MessageResponseTo create(@Valid MessageRequestTo entity);

    List<MessageResponseTo> read();

    @Validated(ValidationMarker.OnUpdate.class)
    MessageResponseTo update(@Valid MessageRequestTo entity);

    void delete(Long id);

    MessageResponseTo findMessageById(Long id);
}
