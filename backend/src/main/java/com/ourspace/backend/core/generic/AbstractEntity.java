package com.ourspace.backend.core.generic;

import java.util.UUID;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

@MappedSuperclass
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public abstract class AbstractEntity {

  @Id
  @GeneratedValue(generator = "uuid2")
  @UuidGenerator
  @Column(columnDefinition = "uuid", name = "id", updatable = false, nullable = false)
  private UUID id;

}