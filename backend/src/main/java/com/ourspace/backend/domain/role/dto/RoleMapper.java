package com.ourspace.backend.domain.role.dto;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import com.ourspace.backend.core.generic.AbstractMapper;
import com.ourspace.backend.domain.role.Role;

/**
 * Mapper for Role.
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RoleMapper extends AbstractMapper<Role, RoleDTO> {
}
