package com.richotaru.notificationapi.dao;

public interface QueryResultTransformer<E, T> {

    T transaform(E e);
}
