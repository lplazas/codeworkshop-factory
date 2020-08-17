package de.conrad.codeworkshop.factory.services.worker;

import de.conrad.codeworkshop.factory.services.factory.Service;
import de.conrad.codeworkshop.factory.services.order.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class OrderWorkerSchedulerTest {

//    @Autowired
//    OrderWorkerScheduler orderScheduler;

    @Autowired
    Service serviceFactory;

    @Autowired
    de.conrad.codeworkshop.factory.services.notification.Service notificationService;

    @Test
    public void testOrderProcessing() throws InterruptedException {

        Order order = new Order();
        serviceFactory.enqueue(order);
        serviceFactory.enqueue(order);
        serviceFactory.enqueue(order);

        Thread.sleep(15100);
        assertEquals(3, notificationService.getCustomerIdsToNotify().size());

    }

}