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
    @Query("SELECT oe FROM OrderEvent as oe GROUP BY oe.order.idOrder, oe.idOrderEvent ORDER BY oe.lastModifiedDate DESC")
    List<OrderEvent> findAllThatDeliveringNow();

    Boolean existsOrderEventsByOrderGuidAndOrderEventTypeName(UUID orderGuid, String eventTypeName);

    List<OrderEvent> getOrderEventsByOrder(IOrder order);
}
