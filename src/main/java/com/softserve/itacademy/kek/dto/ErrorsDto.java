package com.softserve.itacademy.kek.dto;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class ErrorsDto {
    private List<ErrorDto> errors;

    public ErrorsDto() {
        this(new LinkedList<>());
    }

    public ErrorsDto(List<ErrorDto> errors) {
        this.errors = errors;
    }

    public ErrorsDto addError(String errorText, String errorDetails) {
        errors.add(new ErrorDto(errorText, errorDetails));

        return this;
    }

    public ErrorsDto addError(String errorText) {
        return addError(errorText, "");
    }

    public List<ErrorDto> getErrors() {
        return errors;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ErrorsDto)) return false;
        ErrorsDto errorsDto = (ErrorsDto) o;
        return Objects.equals(errors, errorsDto.errors);
    }

    @Override
    public int hashCode() {
        return Objects.hash(errors);
    }
}
