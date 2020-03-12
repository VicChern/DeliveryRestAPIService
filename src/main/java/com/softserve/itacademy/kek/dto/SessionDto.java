package com.softserve.itacademy.kek.dto;

import java.util.Objects;

public class SessionDto {
    private String sessionId;

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public SessionDto() {
    }

    public SessionDto(String sessionId) {
        this.sessionId = sessionId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SessionDto sessionDto = (SessionDto) o;
        return sessionId.equals(sessionDto.sessionId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sessionId);
    }

    @Override
    public String toString() {
        return "SessionId{" +
                "id='" + sessionId + '\'' +
                '}';
    }
}
