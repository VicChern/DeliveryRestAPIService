package com.softserve.itacademy.kek.dto;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class ErrorListDto {
    private List<ErrorDto> errorList;

    public ErrorListDto() {
        this(new LinkedList<>());
    }

    public ErrorListDto(List<ErrorDto> errorList) {
        this.errorList = errorList;
    }

    public ErrorListDto addError(String errorText, String errorDetails) {
        errorList.add(new ErrorDto(errorText, errorDetails));

        return this;
    }

    public ErrorListDto addError(String errorText) {
        return addError(errorText, "");
    }

    public List<ErrorDto> getErrorList() {
        return errorList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ErrorListDto)) return false;
        ErrorListDto errorsDto = (ErrorListDto) o;
        return Objects.equals(errorList, errorsDto.errorList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(errorList);
    }
}
