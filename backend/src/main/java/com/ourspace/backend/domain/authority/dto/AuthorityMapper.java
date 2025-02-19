package com.ourspace.backend.domain.authority.dto;

import com.ourspace.backend.core.generic.AbstractMapper;
import com.ourspace.backend.domain.authority.Authority;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AuthorityMapper extends AbstractMapper<Authority, AuthorityDTO> {
}
