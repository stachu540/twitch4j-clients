package com.github.twitch4j.clients.websocket;

import com.github.twitch4j.clients.event.Event;
import java.util.function.Consumer;
import org.jetbrains.annotations.Nullable;

public interface EventParser {
  Event onOpen();

  Event onClose(int code, @Nullable String reason);

  void onMessage(String raw, Consumer<Event> event);
}
