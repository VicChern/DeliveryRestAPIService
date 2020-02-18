package com.softserve.itacademy.kek.models;

import java.util.UUID;

public interface IActor {

    ITenant getIdTenant();

    IUser getIdUser();

    UUID getGuid();

    String getAlias();
}
