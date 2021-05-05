package com.github.twitch4j.clients.websocket;

import com.github.twitch4j.clients.websocket.event.EventManager;

public abstract class WebSocketClient {
  public abstract EventManager getEventManager();

  public abstract void start();

  public abstract void stop();

  protected abstract void sendRaw(String message);
}
