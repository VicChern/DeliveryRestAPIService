package com.softserve.itacademy.kek.dto;

import java.beans.Transient;
import java.util.Objects;
import java.util.UUID;

import com.softserve.itacademy.kek.models.IActor;

public class ActorDto implements IActor {
    private UUID guid;
    private UUID tenant;
    private UUID user;
    private String alias;

    public ActorDto(UUID guid, UUID tenant, UUID user, String alias) {
        this.tenant = tenant;
        this.user = user;
        this.guid = guid;
        this.alias = alias;
    }

    public ActorDto() {
    }

    @Transient
    @Override
    public TenantDto getTenant() {
        return new TenantDto(tenant, null, null, null);
    }

    @Transient
    @Override
    public UserDto getUser() {
        return new UserDto(user, null, null, null, null, null);
    }

    @Override
    public UUID getGuid() {
        return guid;
    }

    @Override
    public String getAlias() {
        return alias;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ActorDto)) return false;
        ActorDto actorDto = (ActorDto) o;
        return Objects.equals(tenant, actorDto.tenant) &&
                Objects.equals(user, actorDto.user) &&
                Objects.equals(guid, actorDto.guid) &&
                Objects.equals(alias, actorDto.alias);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tenant, user, guid, alias);
    }

    @Override
    public String toString() {
        return "ActorDto{" +
                "tenant=" + tenant +
                ", user=" + user +
                ", guid=" + guid +
                ", alias='" + alias + '\'' +
                '}';
    }
}
