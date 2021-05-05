package com.github.twitch4j.clients.event;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Consumer;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public final class SimpleEventManager implements EventManager {
  private final Map<Class<? extends Event>, Consumer<Event>> events = new LinkedHashMap<>();

  @Override
  @SuppressWarnings("unchecked")
  public final void registerHandler(Object event) {
    EventUtils.fetchEvents(event).forEach((type, result) -> onEvent((Class<Event>) type, (Consumer<Event>) result));
  }

  @Override
  @SuppressWarnings("unchecked")
  public final <T extends Event> void onEvent(Class<T> type, Consumer<T> result) {
    Consumer<Event> event = (Consumer<Event>) result;
    if (events.containsKey(type)) {
      event = events.get(type).andThen(event);
    }
    events.put(type, event);
  }

  @Override
  public final void handle(Event event) {
    events.get(event.getClass()).accept(event);
  }
}
