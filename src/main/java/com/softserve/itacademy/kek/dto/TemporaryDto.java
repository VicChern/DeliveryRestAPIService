package com.softserve.itacademy.kek.dto;

import java.util.Objects;

public class TemporaryDto {

    private String sessionId;

    private String userGuid;

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getUserGuid() {
        return userGuid;
    }

    public void setUserGuid(String userGuid) {
        this.userGuid = userGuid;
    }

    public TemporaryDto() {
    }

    public TemporaryDto(String sessionId, String userGuid) {
        this.sessionId = sessionId;
        this.userGuid = userGuid;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TemporaryDto that = (TemporaryDto) o;
        return Objects.equals(sessionId, that.sessionId) &&
                Objects.equals(userGuid, that.userGuid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sessionId, userGuid);
    }

    @Override
    public String toString() {
        return "TemporaryDto{" +
                "sessionId='" + sessionId + '\'' +
                ", userGuid='" + userGuid + '\'' +
                '}';
    }
}
