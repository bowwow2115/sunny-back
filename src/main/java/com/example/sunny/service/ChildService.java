package com.example.sunny.service;

import com.example.sunny.model.Child;

public interface ChildService extends CrudService<Child, Long> {
    Child findByName(String name);
}
