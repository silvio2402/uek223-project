package com.ourspace.backend.core.generic;

import java.util.List;
import java.util.Set;

/**
 * Generic interface for mapping between Business Objects (BO) and Data
 * Transfer Objects (DTO).
 *
 * @param <BO>  the type of the Business Object
 * @param <DTO> the type of the Data Transfer Object
 */
public interface AbstractMapper<BO extends AbstractEntity, DTO extends AbstractDTO> {

  BO fromDTO(DTO dto);

  List<BO> fromDTOs(List<DTO> dtos);

  Set<BO> fromDTOs(Set<DTO> dtos);

  DTO toDTO(BO BO);

  List<DTO> toDTOs(List<BO> BOs);

  Set<DTO> toDTOs(Set<BO> BOs);
}
