package com.softserve.itacademy.kek.dto;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class GlobalPropertiesListDto {
    @Valid
    @NotNull
    private List<GlobalPropertiesDto> globalPropertiesDtoList;

    public GlobalPropertiesListDto() {
        this(new LinkedList<>());
    }

    public GlobalPropertiesListDto(@Valid @NotNull List<GlobalPropertiesDto> globalPropertiesDtoList) {
        this.globalPropertiesDtoList = globalPropertiesDtoList;
    }

    public List<GlobalPropertiesDto> getGlobalPropertiesDtoList() {
        return globalPropertiesDtoList;
    }

    public GlobalPropertiesListDto addGlobalProperties(GlobalPropertiesDto globalPropertiesDto) {
        globalPropertiesDtoList.add(globalPropertiesDto);

        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GlobalPropertiesListDto that = (GlobalPropertiesListDto) o;
        return Objects.equals(globalPropertiesDtoList, that.globalPropertiesDtoList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(globalPropertiesDtoList);
    }

    @Override
    public String toString() {
        return "GlobalPropertiesListDto{" +
                "globalPropertiesDtoList=" + globalPropertiesDtoList +
                '}';
    }
}
