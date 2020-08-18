package de.conrad.codeworkshop.factory.services.worker;

import de.conrad.codeworkshop.factory.services.factory.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Component
public class OrderWorkerScheduler implements AutoCloseable  {

    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    @Autowired
    public OrderWorkerScheduler(Service factoryService, de.conrad.codeworkshop.factory.services.notification.Service notificationService){
        scheduler.scheduleWithFixedDelay(new OrderWorker(factoryService, notificationService), 10, 100, TimeUnit.MILLISECONDS);
    }

    @Override
    public void close() throws InterruptedException {
        scheduler.shutdown();
        scheduler.awaitTermination(10, TimeUnit.SECONDS);
        if (!scheduler.isTerminated()){
            scheduler.shutdownNow();
        }
    }
}
