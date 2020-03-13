package com.softserve.itacademy.kek.dto;

import javax.validation.Valid;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class KekListDto<T> {
    @Valid
    private List<T> list;

    public KekListDto() {
        this(new LinkedList<T>());
    }

    public KekListDto(@Valid List<T> list) {
        this.list = list;
    }


    public List<T> getList() {
        return list;
    }

    public KekListDto<T> addKekItem(T item) {
        list.add(item);

        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof KekListDto)) return false;
        KekListDto<?> that = (KekListDto<?>) o;
        return Objects.equals(list, that.list);
    }

    @Override
    public int hashCode() {
        return Objects.hash(list);
    }

    @Override
    public String toString() {
        return "ListDto{"
                + "list="
                + list.stream().map(T::toString)
                .collect(Collectors.joining(","))
                + '}';
    }
}
