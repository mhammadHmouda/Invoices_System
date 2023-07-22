package com.harri.training1.services;

import java.util.List;

public interface BaseService <T, ID extends Number> {
    List<T> findAll();

    T findById(ID id);

    void deleteById(ID id);

    void update(T obj);
}
