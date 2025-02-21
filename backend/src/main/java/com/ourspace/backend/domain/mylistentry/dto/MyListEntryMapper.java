package com.ourspace.backend.domain.mylistentry.dto;

import java.util.List;

import org.mapstruct.Mapper;

import com.ourspace.backend.domain.mylistentry.MyListEntry;
import com.ourspace.backend.domain.user.dto.UserMapper;

@Mapper(componentModel = "spring", uses = { UserMapper.class })
public interface MyListEntryMapper {

  MyListEntryDTO toDto(MyListEntry entity);

  MyListEntry toEntity(MyListEntryDTO dto);

  List<MyListEntryDTO> toDtoList(List<MyListEntry> entityList);

  List<MyListEntry> toEntityList(List<MyListEntryDTO> dtoList);
}
