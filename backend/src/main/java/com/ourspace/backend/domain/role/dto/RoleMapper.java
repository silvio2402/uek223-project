package com.ourspace.backend.domain.role.dto;

import com.ourspace.backend.core.generic.AbstractMapper;
import com.ourspace.backend.domain.role.Role;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RoleMapper extends AbstractMapper<Role, RoleDTO> {
}
