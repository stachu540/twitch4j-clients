package com.github.twitch4j.clients.test.event;

import com.github.twitch4j.clients.event.Event;
import lombok.Value;

@Value
public class EventTest implements Event {
  String value;
}
