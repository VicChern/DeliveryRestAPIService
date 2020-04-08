package com.softserve.itacademy.kek.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.softserve.itacademy.kek.dto.PropertyTypeDto;
import com.softserve.itacademy.kek.models.IPropertyType;
import com.softserve.itacademy.kek.models.impl.PropertyType;

/**
 * Interface for {@link PropertyType} mapping
 */
@Mapper
public interface IPropertyTypeMapper {

    IPropertyTypeMapper INSTANCE = Mappers.getMapper(IPropertyTypeMapper.class);

    /**
     * Transform {@link IPropertyType} to {@link PropertyTypeDto}
     *
     * @param propertyType
     * @return propertyTypeDto
     */
    PropertyTypeDto toPropertyTypeDto(IPropertyType propertyType);
}
