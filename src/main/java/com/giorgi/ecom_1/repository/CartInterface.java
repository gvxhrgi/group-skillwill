package com.giorgi.ecom_1.repository;

import com.giorgi.ecom_1.model.Product;

import java.util.Collection;

public interface CartInterface<T> {
    void storeWithUser(String userName, String productName);

    public T retrieve(String name);

    Collection<T> returnAll();

    public Product updateWithUsername(String userName, T t, String productName);

    public T delete(String name);
}
