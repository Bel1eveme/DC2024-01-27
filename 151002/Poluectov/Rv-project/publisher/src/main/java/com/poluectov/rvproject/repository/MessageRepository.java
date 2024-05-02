package com.poluectov.rvproject.repository;

import com.poluectov.rvproject.dto.message.MessageRequestTo;
import com.poluectov.rvproject.model.Message;

public interface MessageRepository extends ICommonRepository<Message, Long> {

    Message update(Message message);
}
