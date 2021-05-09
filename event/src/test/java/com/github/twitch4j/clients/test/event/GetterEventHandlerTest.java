package com.github.twitch4j.clients.test.event;

import com.github.twitch4j.clients.event.EventHandler;
import com.github.twitch4j.clients.event.EventParameter;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class GetterEventHandlerTest {
  @EventHandler(EventTest.class)
  public void getterEventTest(@EventParameter("value") String value) {
    assertEquals("getter_event_test", value);
  }
}
