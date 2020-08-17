package de.conrad.codeworkshop.factory.services.order.validation;

import de.conrad.codeworkshop.factory.services.order.api.Order;

public interface OrderValidator {

    /**
     * Sets the status to the order passed as parameter.
     * The status will be ACCEPTED if the order is valid, DECLINED otherwise
     * @param order order to validate
     */
    void validateOrder(final Order order);
}
