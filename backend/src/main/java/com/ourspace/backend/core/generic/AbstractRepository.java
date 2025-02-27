package com.ourspace.backend.core.generic;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Generic interface for repositories extending JpaRepository.
 *
 * @param <T> the type of the Entity
 */
public interface AbstractRepository<T extends AbstractEntity> extends JpaRepository<T, UUID> {
  Page<T> findAll(Specification<T> spec, Pageable pageable);
}
