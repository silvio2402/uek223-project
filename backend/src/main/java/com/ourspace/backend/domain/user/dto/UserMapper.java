package com.ourspace.backend.domain.user.dto;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import com.ourspace.backend.core.generic.AbstractMapper;
import com.ourspace.backend.domain.user.User;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper extends AbstractMapper<User, UserDTO> {
  User fromUserRegisterDTO(UserRegisterDTO dto);
}
