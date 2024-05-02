package org.education.bean.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessageRequest {
    String message;
    int index;
    List<MessageRequestTo> messages = new ArrayList<>();
}