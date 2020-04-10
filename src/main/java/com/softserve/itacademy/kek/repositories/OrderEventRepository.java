package com.softserve.itacademy.kek.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.softserve.itacademy.kek.models.IOrder;
import com.softserve.itacademy.kek.models.impl.OrderEvent;

/**
 * Repository for work with Order Events
 */
public interface OrderEventRepository extends JpaRepository<OrderEvent, Long> {

    /**
     * Retrieves an order events by its guid.
     *
     * @param guid
     * @return the order event with the given guid
     */
    Optional<OrderEvent> findByGuid(UUID guid);

    /**
     * Retrieves an order by its guid.
     *
     * @param orderGuid
     * @return the distinct order with the given guid or {@literal Optional#empty()} if none found
     */
    Optional<OrderEvent> findDistinctTopByOrderGuidOrderByLastModifiedDateDesc(UUID orderGuid);

    @Query("SELECT OOE " +
            "FROM " +
            "OrderEvent OOE, " +
            "OrderEventType ET " +
            "WHERE " +
            "OOE.lastModifiedDate in (" +
            "SELECT " +
            "MAX(OE.lastModifiedDate) " +
            "FROM OrderEvent OE " +
            "GROUP BY OE.order) " +
            "AND ET.idOrderEventType = OOE.orderEventType " +
            "AND ET.name = :eventTypeName")
    List<OrderEvent> findAllLastAddedOrderEventsForEventType(@Param("eventTypeName") String eventTypeName);

    Boolean existsOrderEventsByOrderGuidAndOrderEventTypeName(UUID orderGuid, String eventTypeName);

    List<OrderEvent> getOrderEventsByOrder(IOrder order);
}