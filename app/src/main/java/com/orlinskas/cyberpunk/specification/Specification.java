package com.orlinskas.cyberpunk.specification;

public interface Specification<T> {
    boolean specified(T object);
}
