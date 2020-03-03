package com.softserve.itacademy.kek.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.softserve.itacademy.kek.models.IOrder;
import com.softserve.itacademy.kek.models.impl.OrderEvent;

public interface OrderEventRepository extends JpaRepository<OrderEvent, Long> {

    OrderEvent findByGuid(UUID guid);

    Optional<OrderEvent> findDistinctTopByOrderGuidOrderByLastModifiedDateDesc(UUID orderGuid);

    // TODO: 02.03.2020 refactor method to find all that delivering now
    @Query(value =
            "SELECT * " +
            "FROM obj_order_event oe " +
            "GROUP BY oe.id_order, oe.id_order_event " +
            "ORDER BY oe.last_modified_date " +
            "DESC " +
            "LIMIT 1"
            , nativeQuery = true)
    List<OrderEvent> findAllThatDeliveringNow();

    Boolean existsOrderEventsByOrderGuidAndOrderEventTypeName(UUID orderGuid, String eventTypeName);

    List<OrderEvent> getOrderEventsByOrder(IOrder order);
}
