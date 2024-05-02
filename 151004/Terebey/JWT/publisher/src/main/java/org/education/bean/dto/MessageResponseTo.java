package org.education.bean.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MessageResponseTo {
    int id;
    int issueId;
    String content;
}