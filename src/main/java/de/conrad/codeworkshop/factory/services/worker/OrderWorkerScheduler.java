package de.conrad.codeworkshop.factory.services.worker;

import de.conrad.codeworkshop.factory.services.factory.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Component
public class OrderWorkerScheduler implements AutoCloseable  {

    private final ExecutorService scheduler = Executors.newSingleThreadExecutor();

    @Autowired
    public OrderWorkerScheduler(Service factoryService, de.conrad.codeworkshop.factory.services.notification.Service notificationService){
        scheduler.submit(new OrderWorker(factoryService, notificationService));
    }

    @Override
    public void close() throws InterruptedException {
        scheduler.shutdown();
        scheduler.awaitTermination(5, TimeUnit.SECONDS);
        if (!scheduler.isTerminated()){
            scheduler.shutdownNow();
        }
    }
}
