package com.github.twitch4j.clients.websocket.event;

import java.util.function.Consumer;

public interface EventManager {
  void registerHandler(Object event);

  <T extends Event> void onEvent(Class<T> type, Consumer<T> result);

  void handle(Event event);
}
