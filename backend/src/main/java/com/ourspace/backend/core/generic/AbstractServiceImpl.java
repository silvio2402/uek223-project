package com.ourspace.backend.core.generic;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import lombok.AllArgsConstructor;

/**
 * Abstract base class for service implementations.
 *
 * @param <T> the type of the Entity
 */
@AllArgsConstructor
public abstract class AbstractServiceImpl<T extends AbstractEntity> implements AbstractService<T> {

  protected final AbstractRepository<T> repository;

  @Override
  public T save(T entity) {
    return repository.save(entity);
  }

  @Override
  public void deleteById(UUID id) throws NoSuchElementException {
    // Check if the entity exists before deleting
    if (repository.existsById(id)) {
      repository.deleteById(id);
    } else {
      throw new NoSuchElementException(String.format("Entity with ID '%s' could not be found", id));
    }
  }

  @Override
  public T updateById(UUID id, T entity) throws NoSuchElementException {
    // Check if the entity exists before updating
    if (repository.existsById(id)) {
      entity.setId(id);
      return repository.save(entity);
    } else {
      throw new NoSuchElementException(String.format("Entity with ID '%s' could not be found", id));
    }
  }

  @Override
  public List<T> findAll() {
    return repository.findAll();
  }

  @Override
  public List<T> findAll(Pageable pageable) {
    Page<T> pagedResult = repository.findAll(pageable);
    return pagedResult.hasContent() ? pagedResult.getContent() : new ArrayList<>();
  }

  @Override
  public T findById(UUID id) {
    return repository.findById(id).orElseThrow(NoSuchElementException::new);
  }

  @Override
  public boolean existsById(UUID id) {
    return repository.existsById(id);
  }

  public Page<T> findAll(Specification<T> spec, Pageable pageable) {
    return repository.findAll(spec, pageable);
  }
}
