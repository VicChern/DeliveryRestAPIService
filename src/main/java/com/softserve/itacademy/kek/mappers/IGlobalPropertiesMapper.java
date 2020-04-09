package com.softserve.itacademy.kek.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import com.softserve.itacademy.kek.models.IGlobalProperty;
import com.softserve.itacademy.kek.models.impl.GlobalProperty;

/**
 * Interface for {@link GlobalProperty} mapping
 */
@Mapper
public interface IGlobalPropertyMapper {

    IGlobalPropertyMapper INSTANCE = Mappers.getMapper(IGlobalPropertyMapper.class);

    /**
     * Transform {@link IGlobalProperty} to {@link GlobalProperty}
     *
     * @param globalProperty
     * @return GlobalProperty
     */
    @Mapping(target = "propertyType", ignore = true)
    GlobalProperty toGlobalProperty(IGlobalProperty globalProperty);
}
