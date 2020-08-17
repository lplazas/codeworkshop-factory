package de.conrad.codeworkshop.factory.services.worker;

import de.conrad.codeworkshop.factory.services.factory.Service;
import de.conrad.codeworkshop.factory.services.order.api.Order;
import de.conrad.codeworkshop.factory.services.order.api.OrderStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OrderWorker implements Runnable {

    private final static Logger LOGGER = LoggerFactory.getLogger(OrderWorker.class);
    private final Service factoryService;
    private final de.conrad.codeworkshop.factory.services.notification.Service notificationService;

    public OrderWorker(Service factoryService, de.conrad.codeworkshop.factory.services.notification.Service notificationService){
        this.factoryService = factoryService;
        this.notificationService = notificationService;
    }

    private final Object lock = new Object();

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()){
            LOGGER.info("Processing new order");
            try {
                Order orderToProcess = factoryService.deque();
                orderToProcess.setStatus(OrderStatus.COMPLETED);
                waitOnLock(5000);
                notificationService.notifyCustomer(orderToProcess);
            } catch (InterruptedException e) {
                LOGGER.error("Error processing order", e);
            }
        }
    }

    public void waitOnLock(int timeoutMillis){
        try {
            synchronized (lock) {
                lock.wait(timeoutMillis);
            }
        } catch (InterruptedException e) {
            LOGGER.error("Error timing out", e);
        }
    }
}
