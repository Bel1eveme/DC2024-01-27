package com.bsuir.nastassiayankova.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.bsuir.nastassiayankova.beans.entity.Message;

public interface MessageRepository extends JpaRepository<Message, Long> {
}
