package com.example.dc_project.service;

import java.util.List;

public interface IService<T, A> {
    A findById(Long id);
    List<A> findAll();
    A create(T requestTo);
    A update(T requestTo);
    boolean removeById(Long id);
}
