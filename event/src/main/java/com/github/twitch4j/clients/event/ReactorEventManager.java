package com.github.twitch4j.clients.event;

import java.util.function.Consumer;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Sinks;

@RequiredArgsConstructor
public final class ReactorEventManager implements EventManager {
  private final Sinks.Many<Event> publisher;

  public ReactorEventManager() {
    this(Sinks.many().multicast().onBackpressureBuffer());
  }

  @Override
  @SuppressWarnings("unchecked")
  public final void registerHandler(Object event) {
    EventUtils.fetchEvents(event).forEach((type, result) -> onEvent((Class<Event>) type, (Consumer<Event>) result));
  }

  @Override
  public final <T extends Event> void onEvent(Class<T> type, Consumer<T> result) {
    publisher.asFlux().ofType(type).subscribe(result);
  }

  @Override
  public final void handle(Event event) {
    publisher.tryEmitNext(event);
  }
}
