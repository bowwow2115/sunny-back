package com.example.sunny.service;

import java.util.List;

public interface CrudService<T, ID> {

    List<T> findAll();

    T findById(ID id);

    T create(T object);

    T update(T object);

    void delete(T object);

    void deleteById(ID id);
}