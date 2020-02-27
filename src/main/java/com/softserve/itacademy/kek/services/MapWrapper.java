package com.softserve.itacademy.kek.services;

import java.util.Map;
import java.util.Objects;
import java.util.UUID;


//temporary added class instead of using generics in Spring Events (this class is used by SSE Controller event Listener)
public class MapWrapper {
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
        if (!(o instanceof MapWrapper)) return false;
        MapWrapper that = (MapWrapper) o;
        return Objects.equals(map, that.map);
    }

    @Override
    public int hashCode() {
        return Objects.hash(map);
    }
}
