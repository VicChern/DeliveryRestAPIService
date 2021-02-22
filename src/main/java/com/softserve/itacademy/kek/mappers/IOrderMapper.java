package com.softserve.itacademy.kek.mappers;

import java.util.UUID;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import com.softserve.itacademy.kek.dto.OrderDto;
import com.softserve.itacademy.kek.models.IOrder;
import com.softserve.itacademy.kek.models.ITenant;
import com.softserve.itacademy.kek.models.impl.Order;

/**
 * Interface for {@link Order} mapping
 */
@Mapper(uses = IOrderDetailsMapper.class)
public interface IOrderMapper {

    IOrderMapper INSTANCE = Mappers.getMapper(IOrderMapper.class);

    /**
     * Returns tenant GUID
     *
     * @param tenant
     * @return UUID
     */
    @Named("getTenantGuid")
    default UUID getTenantGuid(ITenant tenant) {
        return tenant.getGuid();
    }

    /**
     * Transform {@link IOrder} to {@link OrderDto}
     *
     * @param iOrder
     * @return orderDto
     */
    @Mapping(source = "tenant", target = "tenantGuid", qualifiedByName = "getTenantGuid")
    @Mapping(target = "orderDetails", qualifiedByName = "toDto")
    OrderDto toOrderDto(IOrder iOrder);

    /**
     * Transform {@link IOrder} to {@link Order}
     *
     * @param iOrder
     * @return order
     */
    @Mapping(target = "tenant", ignore = true)
    @Mapping(target = "orderDetails", ignore = true)
    Order toOrder(IOrder iOrder);

}
