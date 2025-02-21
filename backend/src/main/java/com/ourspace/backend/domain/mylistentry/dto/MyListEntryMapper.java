package com.ourspace.backend.domain.mylistentry.dto;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.data.domain.Page;

import com.ourspace.backend.domain.mylistentry.MyListEntry;
import com.ourspace.backend.domain.user.dto.UserMapper;

@Mapper(componentModel = "spring", uses = { UserMapper.class })
public interface MyListEntryMapper {

  @Mapping(target = "title", source = "title")
  @Mapping(target = "user_id", source = "user.id")
  MyListEntryDTO toDto(MyListEntry entity);

  MyListEntry toEntity(MyListEntryDTO dto);

  @Mapping(target = "creation_date", ignore = true)
  @Mapping(target = "user", ignore = true)
  MyListEntry toEntity(PostMyListEntryDTO dto);

  List<MyListEntryDTO> toDtoList(Page<MyListEntry> entityList);

  List<MyListEntry> toEntityList(List<MyListEntryDTO> dtoList);
}
