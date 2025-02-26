package com.ourspace.backend.domain.user.dto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

import com.ourspace.backend.core.generic.AbstractMapper;
import com.ourspace.backend.domain.user.User;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper extends AbstractMapper<User, UserDTO> {

  @Mapping(source = "firstName", target = "firstName")
  User fromUserRegisterDTO(UserRegisterDTO dto);

  @Override
  @Mapping(source = "roles", target = "roles")
  UserDTO toDTO(User entity);
}
