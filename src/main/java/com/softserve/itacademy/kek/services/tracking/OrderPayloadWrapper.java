package com.softserve.itacademy.kek.services.tracking;

import java.util.Map;
import java.util.Objects;
import java.util.UUID;


//wrapper, that allow generic Spring events for SSE Controller job
public class OrderPayloadWrapper {
    Map<UUID, String> map;

    public Map<UUID, String> getMap() {
        return map;
    }

    public void setMap(Map<UUID, String> map) {
        this.map = map;
    }

    @Override
    public String toString() {
        return "MapWrapper{" +
                "map=" + map +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OrderPayloadWrapper)) return false;
        OrderPayloadWrapper that = (OrderPayloadWrapper) o;
        return Objects.equals(map, that.map);
    }

    @Override
    public int hashCode() {
        return Objects.hash(map);
    }
}
