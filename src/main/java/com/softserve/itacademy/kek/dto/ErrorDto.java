package com.softserve.itacademy.kek.dto;

import java.util.Objects;

public class ErrorDto {
    private String error;
    private String details;

    public ErrorDto() {
    }

    public ErrorDto(String error, String details) {
        this.error = error;
        this.details = details;
    }

    public String getError() {
        return error;
    }

    public String getDetails() {
        return details;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ErrorDto)) return false;
        ErrorDto errorDto = (ErrorDto) o;
        return Objects.equals(error, errorDto.error) &&
                Objects.equals(details, errorDto.details);
    }

    @Override
    public int hashCode() {
        return Objects.hash(error, details);
    }
}
