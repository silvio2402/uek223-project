package com.ourspace.backend.domain.authority.dto;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import com.ourspace.backend.core.generic.AbstractMapper;
import com.ourspace.backend.domain.authority.Authority;

/**
 * Mapper for authority.
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AuthorityMapper extends AbstractMapper<Authority, AuthorityDTO> {
}
