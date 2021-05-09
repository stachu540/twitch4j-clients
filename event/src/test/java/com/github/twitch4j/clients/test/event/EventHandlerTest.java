package com.github.twitch4j.clients.test.event;

import com.github.twitch4j.clients.event.EventHandler;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class EventHandlerTest {
  @EventHandler
  public void eventTest(EventTest test) {
    assertEquals("event_test", test.getValue());
  }
}
