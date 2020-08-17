package de.conrad.codeworkshop.factory.services.notification;

import de.conrad.codeworkshop.factory.services.order.api.Order;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Andreas Hartmann
 */
@org.springframework.stereotype.Service("notificationService")
public class Service {

    public Collection<Integer> customerIdsToNotify = Collections.synchronizedCollection(new ArrayList<Integer>());
    private AtomicInteger counter = new AtomicInteger(0);
    public void notifyCustomer(final Order order) {
        // Dummy function that would notify the customer that manufacturing is completed.
        customerIdsToNotify.add(counter.getAndIncrement());
    }

    public Collection<Integer> getCustomerIdsToNotify() {
        return customerIdsToNotify;
    }
}
