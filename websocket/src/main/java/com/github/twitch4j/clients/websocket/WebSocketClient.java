package com.github.twitch4j.clients.websocket;

import com.github.twitch4j.clients.event.EventManager;
import java.net.URI;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.jetbrains.annotations.Nullable;

@SuperBuilder
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public final class WebSocketClient {

  private final WebSocketEngine engine;

  @Getter
  private final EventManager eventManager;
  private final EventParser eventParser;
  @Getter
  private final URI url;

  public void start() {
    engine.start(url, eventManager, eventParser);
  }

  public void stop(int code, @Nullable String reason) {
    engine.stop(code, reason);
  }

  public void stop(int code) {
    stop(1000, null);
  }

  public void stop() {
    stop(1000);
  }

  protected void sendRaw(String message) {
    engine.sendRaw(message);
  }
}
