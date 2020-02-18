package com.softserve.itacademy.kek.models;

import java.util.UUID;

public interface IActor {

    ITenant getTenant();

    IUser getUser();

    UUID getGuid();

    String getAlias();
}
