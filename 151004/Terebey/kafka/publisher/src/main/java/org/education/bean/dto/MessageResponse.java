package org.education.bean.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.education.bean.Message;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessageResponse {
    String message;
    int index;
    List<Message> messages = new ArrayList<>();
}