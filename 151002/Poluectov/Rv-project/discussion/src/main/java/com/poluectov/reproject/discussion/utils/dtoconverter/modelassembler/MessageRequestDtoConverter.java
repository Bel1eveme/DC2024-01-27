package com.poluectov.reproject.discussion.utils.dtoconverter.modelassembler;

import com.poluectov.reproject.discussion.dto.message.MessageRequestTo;
import com.poluectov.reproject.discussion.model.Message;
import com.poluectov.reproject.discussion.utils.dtoconverter.DtoConverter;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class MessageRequestDtoConverter implements DtoConverter<MessageRequestTo, Message> {

    @Override
    public Message convert(MessageRequestTo message) {
        return Message.builder()
                .id(message.getId())
                .issueId(message.getIssueId())
                .content(message.getContent())
                .country(message.getCountry())
                .build();
    }
}
