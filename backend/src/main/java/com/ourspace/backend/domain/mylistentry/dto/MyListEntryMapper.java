package com.ourspace.backend.domain.mylistentry.dto;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import org.springframework.data.domain.Page;

import com.ourspace.backend.domain.mylistentry.MyListEntry;
import com.ourspace.backend.domain.user.dto.UserMapper;

/**
 * Mapper for MyListEntry.
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = UserMapper.class, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface MyListEntryMapper {

  @Mapping(target = "user_id", source = "user.id")
  MyListEntryDTO toDto(MyListEntry entity);

  MyListEntry toEntity(MyListEntryDTO dto);

  MyListEntry toEntity(PostMyListEntryDTO dto);

  List<MyListEntryDTO> toDtoList(Page<MyListEntry> entityList);

  List<MyListEntry> toEntityList(List<MyListEntryDTO> dtoList);
}
