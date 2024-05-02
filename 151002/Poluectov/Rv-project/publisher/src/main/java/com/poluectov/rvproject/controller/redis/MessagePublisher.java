package com.poluectov.rvproject.controller.redis;

public interface MessagePublisher<M> {

    void publish(M message);
}
