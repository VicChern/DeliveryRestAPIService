package com.softserve.itacademy.kek.services;

import java.util.UUID;

import com.softserve.itacademy.kek.repositories.ActorRepository;
import org.mockito.ArgumentCaptor;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.softserve.itacademy.kek.models.IOrder;
import com.softserve.itacademy.kek.models.impl.Order;
import com.softserve.itacademy.kek.models.impl.Tenant;
import com.softserve.itacademy.kek.repositories.OrderEventRepository;
import com.softserve.itacademy.kek.repositories.OrderEventTypeRepository;
import com.softserve.itacademy.kek.repositories.OrderRepository;
import com.softserve.itacademy.kek.repositories.TenantRepository;
import com.softserve.itacademy.kek.services.impl.OrderServiceImpl;

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
@Test(groups = {"unit-tests"})
//@Test
public class OrderServiceTest {

    private IOrderService orderService;

    private OrderRepository orderRepository;

    private TenantRepository tenantRepository;

    private OrderEventRepository orderEventRepository;
    private ActorRepository actorRepository;

    private OrderEventTypeRepository orderEventTypeRepository;
    private IUserService userService;
    private ITenantService tenantService;

    @BeforeClass
    public void setUp() {
        orderRepository = mock(OrderRepository.class);
        tenantRepository = mock(TenantRepository.class);
        orderEventRepository = mock(OrderEventRepository.class);
        orderEventTypeRepository = mock(OrderEventTypeRepository.class);
        actorRepository = mock(ActorRepository.class);
        userService = mock(IUserService.class);
        tenantService = mock(ITenantService.class);
        orderService = new OrderServiceImpl(orderRepository, tenantRepository, orderEventRepository, actorRepository, orderEventTypeRepository, userService, tenantService);
    }

    @AfterMethod
    void tearDown() {
        reset(orderRepository, tenantRepository, orderEventRepository);
    }

    @Test
    public void createOrderSuccess() throws Exception {
        Order testOrder = createOrderForTest(1L, null);

        when(orderRepository.save(any(Order.class))).thenReturn(testOrder);

//        IOrder createdOrder = orderService.create(testOrder);

        ArgumentCaptor<Order> acOrder = ArgumentCaptor.forClass(Order.class);

        verify(orderRepository, times(1)).save(any(Order.class));
        verify(orderRepository).save(acOrder.capture());

        Order actualOrder = acOrder.getValue();

//        Assert.assertNotNull(createdOrder);
        Assert.assertNotNull(actualOrder.getGuid());
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

        orderService.deleteByGuid(guid);

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
