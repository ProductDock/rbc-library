package com.productdock.book;

import java.util.List;

public interface DtoMapper<DTO, D> {

    D toDomain(DTO dto);

    DTO toDto(D domain);

    List<D> toDomain(List<DTO> dtoList);

    List<DTO> toDto(List<D> domainList);

}
