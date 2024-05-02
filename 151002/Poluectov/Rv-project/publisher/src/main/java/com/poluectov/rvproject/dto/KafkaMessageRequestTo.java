package com.poluectov.rvproject.dto;

import com.poluectov.rvproject.model.Message;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    Message body;

}
