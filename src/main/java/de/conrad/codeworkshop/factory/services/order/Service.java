package de.conrad.codeworkshop.factory.services.order;

import de.conrad.codeworkshop.factory.services.factory.Controller;
import de.conrad.codeworkshop.factory.services.order.api.Order;
import de.conrad.codeworkshop.factory.services.order.api.OrderConfirmation;
import de.conrad.codeworkshop.factory.services.order.api.OrderNumber;
import de.conrad.codeworkshop.factory.services.order.validation.OrderValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;

import java.math.BigInteger;
import java.util.Random;

import static de.conrad.codeworkshop.factory.services.order.api.OrderConfirmation.BLANK_ORDER_CONFIRMATION;
import static de.conrad.codeworkshop.factory.services.order.api.OrderStatus.ACCEPTED;

/**
 * @author Andreas Hartmann
 */
@org.springframework.stereotype.Service("orderService")
public class Service {

    private final Controller factoryController;

    private final OrderValidator orderValidator;

    @Autowired
    public Service(de.conrad.codeworkshop.factory.services.factory.Controller factoryController, OrderValidator orderValidator) {
        this.factoryController = factoryController;
        this.orderValidator = orderValidator;
    }

    /**
     * Validates the input order. If it is valid, it is enqueued in the factory via the factoryController. Either way an
     * appropriate order confirmation is returned.
     */
    @PostMapping("/create")
    public OrderConfirmation createOrder(final Order order) {
        orderValidator.validateOrder(order);

        final OrderConfirmation orderConfirmation;

        if (order.getStatus() == ACCEPTED) {
            orderConfirmation = new OrderConfirmation(new OrderNumber(BigInteger.valueOf(new Random().nextInt())));

            order.setOrderConfirmation(orderConfirmation);

            factoryController.enqueue(order);
        } else {
            orderConfirmation = BLANK_ORDER_CONFIRMATION;
        }

        return orderConfirmation;
    }
}
