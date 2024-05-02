package com.poluectov.rvproject.dto;

import com.poluectov.rvproject.model.Message;
import com.poluectov.rvproject.model.RestError;
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

    List<Message> messages;

    RestError error;
}
