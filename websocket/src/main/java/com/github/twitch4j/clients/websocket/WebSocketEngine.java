package com.github.twitch4j.clients.websocket;

import com.github.twitch4j.clients.event.EventManager;
import java.net.URI;
import org.jetbrains.annotations.Nullable;

public interface WebSocketEngine {

  void start(URI url, EventManager manager, EventParser parser);

  void stop(int code, @Nullable String reason);

  void sendRaw(String message);
}
