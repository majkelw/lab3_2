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
    void setUp() {
        when(clock.getZone()).thenReturn(ZoneId.systemDefault());
        order = new Order(clock);
    }

    @Test
    void testOrderConfirmWithInvalidPeriodShouldThrowOrderExpiredException() {
        int timeAfterTwoDays = 48;
        Instant start = Instant.now();
        Instant end = start.plus(timeAfterTwoDays, ChronoUnit.HOURS);
        when(clock.instant()).thenReturn(start).thenReturn(end);
        order.submit();
        assertThrows(OrderExpiredException.class, () -> order.confirm());
    }

    @Test
    void testOrderWithValidPeriodShouldBeConfirmed() {
        int timeAfterHalfDay = 12;
        Instant start = Instant.now();
        Instant end = start.plus(timeAfterHalfDay, ChronoUnit.HOURS);
        when(clock.instant()).thenReturn(start).thenReturn(end);
        order.submit();
        order.confirm();
        assertEquals(Order.State.CONFIRMED, order.getOrderState());
    }

    @Test
    void testOrderWithValidPeriodShouldBeRealized() {
        int timeAfterDay = 24;
        Instant start = Instant.now();
        Instant end = start.plus(timeAfterDay, ChronoUnit.HOURS);
        when(clock.instant()).thenReturn(start).thenReturn(end);
        order.submit();
        order.confirm();
        order.realize();
        assertEquals(Order.State.REALIZED, order.getOrderState());
    }

    @Test
    void testOrderConfirmWithInvalidPeriodShouldBeCancelled() {
        int timeAfterFourDays = 96;
        Instant start = Instant.now();
        Instant end = start.plus(timeAfterFourDays, ChronoUnit.HOURS);
        when(clock.instant()).thenReturn(start).thenReturn(end);
        order.submit();
        assertThrows(OrderExpiredException.class, () -> order.confirm());
        assertEquals(Order.State.CANCELLED, order.getOrderState());
    }

    @Test
    void testOrderWithoutConfirmationShouldThrowOrderStateException() {
        Instant start = Instant.now();
        int timeAfterDay = 24;
        Instant end = start.plus(timeAfterDay, ChronoUnit.HOURS);
        when(clock.instant()).thenReturn(start).thenReturn(end);
        order.submit();
        assertThrows(OrderStateException.class, () -> order.realize());
    }

}
