package com.github.twitch4j.clients.event;

import io.reactivex.rxjava3.subjects.PublishSubject;
import java.util.function.Consumer;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public final class ReactiveXEventManager implements EventManager {
  private final PublishSubject<Event> publisher;

  public ReactiveXEventManager() {
    this(PublishSubject.create());
  }

  @Override
  @SuppressWarnings("unchecked")
  public final void registerHandler(Object event) {
    EventUtils.fetchEvents(event).forEach((type, result) -> onEvent((Class<Event>) type, (Consumer<Event>) result));
  }

  @Override
  public final <T extends Event> void onEvent(Class<T> type, Consumer<T> result) {
    publisher.ofType(type).subscribe(result::accept);
  }

  @Override
  public final void handle(Event event) {
    publisher.onNext(event);
  }
}
