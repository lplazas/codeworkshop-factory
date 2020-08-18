package de.conrad.codeworkshop.factory.services.worker;

import de.conrad.codeworkshop.factory.services.factory.Service;
import de.conrad.codeworkshop.factory.services.order.api.Order;
import de.conrad.codeworkshop.factory.services.order.api.OrderStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class OrderWorkerSchedulerTest {

    @Autowired
    Service serviceFactory;

    @Autowired
    de.conrad.codeworkshop.factory.services.notification.Service notificationService;

    @Test
    public void testOrderProcessing() throws InterruptedException {

        Order order1 = new Order();
        serviceFactory.enqueue(order1);
        assertEquals(OrderStatus.IN_PROGRESS, order1.getStatus());
        Order order2 = new Order();
        serviceFactory.enqueue(order2);
        assertEquals(OrderStatus.IN_PROGRESS, order2.getStatus());
        Order order3 = new Order();
        serviceFactory.enqueue(order3);
        assertEquals(OrderStatus.IN_PROGRESS, order3.getStatus());

        Thread.sleep(5500);
        assertEquals(1, notificationService.getCustomerIdsToNotify().size());
        assertEquals(OrderStatus.COMPLETED, order1.getStatus());

        Thread.sleep(5500);
        assertEquals(2, notificationService.getCustomerIdsToNotify().size());
        assertEquals(OrderStatus.COMPLETED, order2.getStatus());

        Thread.sleep(5500);
        assertEquals(3, notificationService.getCustomerIdsToNotify().size());
        assertEquals(OrderStatus.COMPLETED, order3.getStatus());
    }

}
