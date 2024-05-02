package com.poluectov.rvproject.utils.dtoconverter;

import com.poluectov.rvproject.dto.message.MessageRequestTo;
import com.poluectov.rvproject.model.Message;
import com.poluectov.rvproject.repository.IssueRepository;
import com.poluectov.rvproject.repository.exception.EntityNotFoundException;
import com.poluectov.rvproject.repository.jpa.JpaIssueRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
public class MessageRequestDtoConverter implements DtoConverter<MessageRequestTo, Message> {
    @Override
    public Message convert(MessageRequestTo message){

        return Message.builder()
                .id(message.getId())
                .issueId(message.getIssueId())
                .content(message.getContent())
                .country(message.getCountry())
                .build();
    }
}
