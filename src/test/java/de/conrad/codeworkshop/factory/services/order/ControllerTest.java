package de.conrad.codeworkshop.factory.services.order;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.conrad.codeworkshop.factory.services.order.api.Order;
import de.conrad.codeworkshop.factory.services.order.api.Position;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import static org.assertj.core.api.Assertions.*;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class ControllerTest {

    public final static String CREATE_ORDER_PATH = "/order/create";

    @Autowired
    private Controller controller;

    @Autowired
    private MockMvc mockMvc;

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void contextLoads(){
        assertThat(controller).isNotNull();
    }

    @Test
    public void testAcceptAndProduceJson() throws Exception {
        Order testOrder = new Order();
        testOrder.setPositions(Collections.emptyList());

        mockMvc.perform(MockMvcRequestBuilders.post("/order/create")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(MAPPER.writeValueAsBytes(testOrder))
                .accept(MediaType.APPLICATION_JSON_VALUE)
        ).andExpect(status().isOk());

        mockMvc.perform(MockMvcRequestBuilders.post(CREATE_ORDER_PATH)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(MAPPER.writeValueAsBytes(testOrder))
                .accept(MediaType.TEXT_PLAIN) //Should be not acceptable
        ).andExpect(status().isNotAcceptable());

        mockMvc.perform(MockMvcRequestBuilders.post(CREATE_ORDER_PATH)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content("thisIsPlainTextShouldBeABadRequest")
                .accept(MediaType.APPLICATION_JSON_VALUE)
        ).andExpect(status().isBadRequest());

        mockMvc.perform(MockMvcRequestBuilders.post(CREATE_ORDER_PATH)
                .contentType(MediaType.TEXT_PLAIN)
                .content("thisIsPlainTextRequestShouldBeUnsuppostedMediaType")
                .accept(MediaType.APPLICATION_JSON_VALUE)
        ).andExpect(status().isUnsupportedMediaType());
    }

    @Test
    public void testOrderValidationLogic() throws Exception {
        Order testOrder = new Order();
        Position shortIdPosition = new Position();
        shortIdPosition.setProductId(14485);
        shortIdPosition.setQuantity(new BigDecimal(10));
        testOrder.setPositions(Collections.singletonList(shortIdPosition));

        performPostCreateOrderAction(testOrder).andExpect(status().isOk())
                .andDo(result -> System.out.println(result.getResponse().getContentAsString()))
                .andExpect(jsonPath("status", is("DECLINED")));

        Position validPosition = new Position();
        validPosition.setProductId(1574854);
        validPosition.setQuantity(new BigDecimal(20));
        testOrder.setPositions(Collections.singletonList(validPosition));
        performPostCreateOrderAction(testOrder).andExpect(status().isOk())
                .andExpect(jsonPath("status", is("ACCEPTED")));

        testOrder.setPositions(Arrays.asList(shortIdPosition, validPosition));
        performPostCreateOrderAction(testOrder).andExpect(status().isOk())
                .andExpect(jsonPath("status", is("DECLINED")));

        validPosition = new Position();
        validPosition.setProductId(1574854);
        validPosition.setQuantity(new BigDecimal("42.42"));
        testOrder.setPositions(Collections.singletonList(validPosition));
        performPostCreateOrderAction(testOrder).andExpect(status().isOk())
                .andExpect(jsonPath("status", is("ACCEPTED")));
    }

    private ResultActions performPostCreateOrderAction(Order testOrder) throws Exception {
        return mockMvc.perform(MockMvcRequestBuilders.post(CREATE_ORDER_PATH)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(MAPPER.writeValueAsBytes(testOrder))
                .accept(MediaType.APPLICATION_JSON_VALUE)
        );
    }
}