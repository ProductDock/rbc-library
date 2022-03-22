package com.productdock.book;

import java.util.List;

public interface EntityMapper<D, E> {

    E toEntity(D domain);

    D toDomain(E entity);

    List<E> toEntity(List<D> domainList);

    List<D> toDomain(List<E> entityList);

}
