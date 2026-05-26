package com.dormitory.management.dao;

import java.util.List;
import java.util.Optional;

public interface BaseDao<T, ID> {
    T create(T entity);

    Optional<T> findById(ID id);

    List<T> findAll();

    T update(T entity);

    void deleteById(ID id);
}

// This is a generic DAO interface that defines basic CRUD operations for any entity type T with an identifier of type ID. The methods include create, findById, findAll, update, and deleteById. Implementations of this interface will provide the actual database interactions for specific entities in the dormitory management system.//