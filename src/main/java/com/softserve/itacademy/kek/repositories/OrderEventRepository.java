package com.softserve.itacademy.kek.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.softserve.itacademy.kek.models.IOrder;
import com.softserve.itacademy.kek.models.impl.OrderEvent;

public interface OrderEventRepository extends JpaRepository<OrderEvent, Long> {

    OrderEvent findByGuid(UUID guid);

    Optional<OrderEvent> findDistinctTopByOrderGuidOrderByLastModifiedDateDesc(UUID orderGuid);

    @Query(value =
            "SELECT OOE.* " +
                "FROM " +
                    "obj_order_event OOE, " +
                    "def_order_event_type ET, " +
                    "(SELECT " +
                            "OE.id_order, " +
                            "MAX(OE.last_modified_date) lastDate " +
                        "FROM obj_order_event OE " +
                        "GROUP BY  OE.id_order" +
                    ") temporaryTable " +
                "WHERE " +
                    "    OOE.last_modified_date = temporaryTable.lastDate " +
                    "AND ET.id_order_event_type = OOE.id_order_event_type " +
                    "AND ET.name = :eventTypeName"
            , nativeQuery = true)
    List<OrderEvent> findAllLastAddedOrderEventsForEventType(@Param("eventTypeName" ) String eventTypeName);

    Boolean existsOrderEventsByOrderGuidAndOrderEventTypeName(UUID orderGuid, String eventTypeName);

    List<OrderEvent> getOrderEventsByOrder(IOrder order);
}