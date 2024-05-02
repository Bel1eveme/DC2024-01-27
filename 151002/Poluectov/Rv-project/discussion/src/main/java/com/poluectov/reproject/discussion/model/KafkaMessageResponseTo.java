package com.poluectov.reproject.discussion.model;

import com.poluectov.reproject.discussion.dto.message.MessageResponseTo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class KafkaMessageResponseTo {

    UUID requestId;

    String status;

    Map<String, String> params;

    List<MessageResponseTo> messages;

    RestError error;
}
