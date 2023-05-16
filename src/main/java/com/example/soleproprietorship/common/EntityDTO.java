package com.example.soleproprietorship.common;

public abstract class EntityDTO<ENTITY extends HasModel, CREATION_DTO, DTO> {


    protected abstract DTO mapEntityToDTO(ENTITY entity);

    protected abstract ENTITY mapCreationDTOToEntity(CREATION_DTO creationDto);


}
