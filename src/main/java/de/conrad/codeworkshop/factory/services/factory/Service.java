package de.conrad.codeworkshop.factory.services.factory;

import de.conrad.codeworkshop.factory.services.order.api.Order;
import de.conrad.codeworkshop.factory.services.order.api.OrderStatus;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * @author Andreas Hartmann
 */
@org.springframework.stereotype.Service("factoryService")
public class Service {

    @SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
    private final BlockingQueue<Order> manufacturingQueue = new LinkedBlockingDeque<>();

    public void enqueue(final Order order) {
        order.setStatus(OrderStatus.IN_PROGRESS);
        manufacturingQueue.add(order);
    }

    public Order deque() throws InterruptedException {
        return manufacturingQueue.take();
    }
}
