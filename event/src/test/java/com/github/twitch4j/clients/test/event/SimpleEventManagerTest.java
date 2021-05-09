package com.github.twitch4j.clients.test.event;

import com.github.twitch4j.clients.event.SimpleEventManager;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class SimpleEventManagerTest {
  protected static SimpleEventManager eventManager;

  @BeforeEach
  public void prepare() {
    eventManager = new SimpleEventManager();
  }

  @Test
  @DisplayName("Consumer Check")
  public void consumerCheck() {
    assertDoesNotThrow(() -> {
      eventManager.registerHandler(new EventConsumerTest());
      eventManager.handle(new EventTest("accept"));
    });
  }

  @Test
  @DisplayName("Method Check")
  public void methodCheck() {
    assertDoesNotThrow(() -> {
      eventManager.registerHandler(new EventHandlerTest());
      eventManager.handle(new EventTest("event_test"));
    });
  }

  @Test
  @DisplayName("Method properties as getter Check")
  public void methodPropertiesAsGetterCheck() {
    assertDoesNotThrow(() -> {
      eventManager.registerHandler(new GetterEventHandlerTest());
      eventManager.handle(new EventTest("getter_event_test"));
    });
  }

  @Test
  @DisplayName("Ordinal event handler Check")
  public void ordinalEventHandlerCheck() {
    assertDoesNotThrow(() -> {
      eventManager.onEvent(EventTest.class, event -> assertEquals(event.getValue(), "ordinal_event"));
      eventManager.handle(new EventTest("ordinal_event"));
    });
  }
}
