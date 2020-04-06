package com.softserve.itacademy.kek.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.softserve.itacademy.kek.dto.OrderDto;
import com.softserve.itacademy.kek.models.IOrder;
import com.softserve.itacademy.kek.models.impl.Order;

/**
 * Interface for {@link Order} mapping
 */
@Mapper(uses = IOrderDetailsMapper.class)
public interface IOrderMapper {

    IOrderMapper INSTANCE = Mappers.getMapper(IOrderMapper.class);

//    /**
//     * Returns tenant GUID
//     *
//     * @param tenant
//     * @return UUID
//     */
//    @Named("TenantToUUID")
//    default UUID getTenantGuid(ITenant tenant) {
//        return tenant.getGuid();
//    }
//
//    /**
//     * Transform {@link IOrder} to {@link OrderDto}
//     *
//     * @param order
//     * @return orderDto
//     */
//    @Mapping(source = "tenant", target = "tenantGuid", qualifiedByName = "TenantToUUID")
    OrderDto toOrderDto(IOrder order);

}
