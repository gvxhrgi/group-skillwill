package com.giorgi.ecom_1.repository;

import com.giorgi.ecom_1.model.Product;

import java.util.Collection;

public interface ProductInterface<T> {
    public void store(T t);

    public T retrieve(String name);

    Collection<T> returnAll();

    public Product update(String name, T t);

    public T delete(String name);
}
