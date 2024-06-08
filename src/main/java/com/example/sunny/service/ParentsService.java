package com.example.sunny.service;

import com.example.sunny.model.Parents;

public interface ParentsService extends CrudService<Parents, Long> {
    Parents findByName(String name);
}
