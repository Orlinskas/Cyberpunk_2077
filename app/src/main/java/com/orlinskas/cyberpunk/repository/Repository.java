package com.orlinskas.cyberpunk.repository;

import com.orlinskas.cyberpunk.specification.SqlSpecification;

import java.util.ArrayList;

public interface Repository<T> {
    void add(T object);
    void update(T object);
    void remote(T object);
    ArrayList<T> query(SqlSpecification sqlSpecification);
}
