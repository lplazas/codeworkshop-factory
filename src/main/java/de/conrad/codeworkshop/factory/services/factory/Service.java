package de.conrad.codeworkshop.factory.services.factory;

import de.conrad.codeworkshop.factory.services.order.api.Order;
import de.conrad.codeworkshop.factory.services.order.api.OrderStatus;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * @author Andreas Hartmann
 */
@org.springframework.stereotype.Service("factoryService")
public class Service {

    @SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
    private Queue<Order> manufacturingQueue = new ConcurrentLinkedQueue<>();

    public void enqueue(final Order order) {
        order.setStatus(OrderStatus.IN_PROGRESS);
        manufacturingQueue.add(order);
    }

    public Order getOrderToProcess() {
        return manufacturingQueue.poll();
    }
}
