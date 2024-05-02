package com.poluectov.reproject.discussion.model;

import com.poluectov.reproject.discussion.dto.message.MessageRequestTo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class KafkaMessageRequestTo {

    UUID id;

    String method;

    Map<String, String> params;

    MessageRequestTo body;

}
