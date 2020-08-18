package de.conrad.codeworkshop.factory.services.order.validation;

import de.conrad.codeworkshop.factory.services.order.api.Order;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;

import java.math.BigDecimal;

import static de.conrad.codeworkshop.factory.services.order.api.OrderStatus.*;

@Controller
@Lazy
public class InterviewOrderValidator implements OrderValidator{

    @Override
    public void validateOrder(Order order) {
        boolean allPositionsAreValid = order.getPositions() != null &&
                order.getPositions().stream()
                .filter(pos -> {
                    int length = (int) Math.log10(pos.getProductId()) + 1;
                    return length >= 6 && length <= 9;
                })
                .filter(pos -> pos.getQuantity().compareTo(BigDecimal.ZERO) > 0 ||
                        pos.getQuantity().compareTo(new BigDecimal("42.42")) == 0 ||
                        pos.getQuantity().remainder(BigDecimal.TEN).compareTo(BigDecimal.ZERO) == 0
                ).count() == order.getPositions().size();
        if (allPositionsAreValid && !order.getPositions().isEmpty() && order.getStatus() == PENDING) {
            order.setStatus(ACCEPTED);
        } else {
            order.setStatus(DECLINED);
        }
    }
}
