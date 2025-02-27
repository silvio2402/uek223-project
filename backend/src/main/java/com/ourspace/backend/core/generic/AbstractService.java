package com.ourspace.backend.core.generic;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

/**
 * Generic interface for services.
 *
 * @param <T> the type of the Entity
 */
public interface AbstractService<T extends AbstractEntity> {

  T save(T entity);

  void deleteById(UUID id) throws NoSuchElementException;

  T updateById(UUID id, T entity) throws NoSuchElementException;

  List<T> findAll();

  List<T> findAll(Pageable pageable);

  T findById(UUID id);

  boolean existsById(UUID id);

  Page<T> findAll(Specification<T> spec, Pageable pageable);
}
