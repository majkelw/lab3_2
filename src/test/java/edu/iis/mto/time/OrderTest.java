package edu.iis.mto.time;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;

@ExtendWith(MockitoExtension.class)
class OrderTest {


    @Mock
    private Clock clock;
    private Order order;

    @BeforeEach
    void setUp(){
        when(clock.getZone()).thenReturn(ZoneId.systemDefault());
        order = new Order(clock);
    }

    @Test
    void testOrderConfirmWithInvalidPeriodShouldThrowOrderExpiredException(){
        int timeAfterTwoDays = 48;
        Instant start = Instant.now();
        Instant end = start.plus(timeAfterTwoDays, ChronoUnit.HOURS);
        when(clock.instant()).thenReturn(start).thenReturn(end);
        order.submit();
        assertThrows(OrderExpiredException.class, () -> order.confirm());
        assertEquals(order.getOrderState(), Order.State.CANCELLED);
    }


}
