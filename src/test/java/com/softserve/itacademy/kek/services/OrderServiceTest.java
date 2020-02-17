package com.softserve.itacademy.kek.services;

import com.softserve.itacademy.kek.dataexchange.IOrder;
import com.softserve.itacademy.kek.models.Order;
import com.softserve.itacademy.kek.models.Tenant;
import com.softserve.itacademy.kek.repositories.OrderRepository;
import com.softserve.itacademy.kek.repositories.TenantRepository;
import com.softserve.itacademy.kek.services.impl.OrderServiceImpl;
import org.mockito.ArgumentCaptor;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.UUID;

import static com.softserve.itacademy.kek.utils.ITCreateEntitiesUtils.createOrdinaryTenant;
import static com.softserve.itacademy.kek.utils.ITCreateEntitiesUtils.getOrder;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Unit test User Service {@link OrderServiceImpl}
 */
//@Test(groups = {"unit-tests"})
@Test
public class OrderServiceTest {

    private IOrderService orderService;

    private OrderRepository orderRepository;

    private TenantRepository tenantRepository;

    @BeforeClass
    public void setUp() {
        orderRepository = mock(OrderRepository.class);
        tenantRepository = mock(TenantRepository.class);
        orderService = new OrderServiceImpl(orderRepository, tenantRepository);
    }

    @AfterMethod
    void tearDown() {
        reset(orderRepository, tenantRepository);
    }

    @Test
    public void createOrderSuccess() throws Exception {
        Order testOrder = createOrderForTest(1L, null);

        when(orderRepository.save(any(Order.class))).thenReturn(testOrder);

        IOrder createdOrder = orderService.save(testOrder);

        ArgumentCaptor<Order> acOrder = ArgumentCaptor.forClass(Order.class);

        verify(orderRepository, times(1)).save(any(Order.class));
        verify(orderRepository).save(acOrder.capture());

        Order actualOrder = acOrder.getValue();

        Assert.assertNotNull(createdOrder);
        Assert.assertNotNull(actualOrder.getIdOrder());
        Assert.assertNotNull(actualOrder.getOrderDetails());
    }

    @Test
    public void updateOrderSuccess() throws Exception {
        Long id = 1L;
        UUID guid = UUID.randomUUID();
        Order testOrder = createOrderForTest(id, guid);
        Order foundOrder = createOrderForTest(id, guid);

        when(orderRepository.findByGuid(guid)).thenReturn(foundOrder);
        when(orderRepository.save(any(Order.class))).thenReturn(testOrder);

        IOrder updatedOrder = orderService.update(testOrder, testOrder.getGuid());

        ArgumentCaptor<Order> acOrder = ArgumentCaptor.forClass(Order.class);

        verify(orderRepository, times(1)).save(any(Order.class));
        verify(orderRepository).save(acOrder.capture());

        Order actualOrder = acOrder.getValue();

        Assert.assertNotNull(updatedOrder);
        Assert.assertNotNull(actualOrder.getIdOrder());
        Assert.assertNotNull(actualOrder.getOrderDetails());
    }

    @Test
    public void deleteOrderSuccess() throws Exception {
        Long id = 1L;
        UUID guid = UUID.randomUUID();
        Order foundOrder = createOrderForTest(id, guid);

        when(orderRepository.findByGuid(guid)).thenReturn(foundOrder);

        orderService.delete(guid);

        ArgumentCaptor<Long> acOrderID = ArgumentCaptor.forClass(Long.class);

        verify(orderRepository, times(1)).deleteById(any(Long.class));
        verify(orderRepository).deleteById(acOrderID.capture());

        Long deletedOrderId = acOrderID.getValue();

        Assert.assertEquals(id, deletedOrderId);
    }

    private Order createOrderForTest(Long id, UUID guid) {
        Tenant tenant = createOrdinaryTenant(1);
        Order order = getOrder(tenant);

        order.setIdOrder(id);
        if (guid != null)
            order.setGuid(guid);

        return order;
    }
}
