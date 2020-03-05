package com.softserve.itacademy.kek.services;

import java.util.Map;
import java.util.Objects;
import java.util.UUID;


//temporary added class, will be removed when use generics in Spring Events for SSE Controller job
public class OrderTrackingWrapper {
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
        if (!(o instanceof OrderTrackingWrapper)) return false;
        OrderTrackingWrapper that = (OrderTrackingWrapper) o;
        return Objects.equals(map, that.map);
    }

    @Override
    public int hashCode() {
        return Objects.hash(map);
    }
}
