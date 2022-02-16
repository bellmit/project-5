package com.kindminds.drs.api.v2.biz.domain.model;

import com.kindminds.drs.Filter;

import java.util.*;


public interface Repository<T> {

    void add(T item);

    void add(List<T> items);

    void edit(T item);

    void remove(T item);

    Optional<T> findById(String id);

    Optional<T> findOne(Filter filter) ;

    List<T> find(Filter filter) ;
}


