package com.bsuir.kirillpastukhou.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.bsuir.kirillpastukhou.domain.entity.Message;

public interface MessageRepository extends JpaRepository<Message, Long> {
}
