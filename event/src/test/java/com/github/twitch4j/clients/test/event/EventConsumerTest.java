package com.github.twitch4j.clients.test.event;

import java.util.function.Consumer;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class EventConsumerTest implements Consumer<EventTest> {
  @Override
  public void accept(EventTest test) {
    assertEquals("accept", test.getValue());
  }
}
