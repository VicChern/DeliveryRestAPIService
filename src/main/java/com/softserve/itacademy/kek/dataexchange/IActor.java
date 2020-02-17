package com.softserve.itacademy.kek.dataexchange;

import java.util.UUID;

public interface IActor {

    ITenant getIdTenant();

    IUser getIdUser();

    UUID getGuid();

    String getAlias();
}
