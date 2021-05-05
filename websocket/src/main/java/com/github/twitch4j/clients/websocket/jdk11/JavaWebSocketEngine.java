package com.github.twitch4j.clients.websocket.jdk11;

import com.github.twitch4j.clients.event.EventManager;
import com.github.twitch4j.clients.websocket.EventParser;
import com.github.twitch4j.clients.websocket.WebSocketEngine;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.WebSocket;
import java.nio.channels.AlreadyConnectedException;
import java.nio.channels.NotYetConnectedException;
import java.util.concurrent.CompletionStage;
import org.jetbrains.annotations.Nullable;

public final class JavaWebSocketEngine implements WebSocketEngine {
  private WebSocket ws = null;

  @Override
  public final void start(URI url, EventManager manager, EventParser parser) {
    if (ws != null) throw new AlreadyConnectedException();
    HttpClient.newHttpClient().newWebSocketBuilder().buildAsync(url, new WebSocket.Listener() {
      @Override
      public void onOpen(WebSocket webSocket) {
        manager.handle(parser.onOpen());
        WebSocket.Listener.super.onOpen(webSocket);
      }

      @Override
      public CompletionStage<?> onText(WebSocket webSocket, CharSequence data, boolean last) {
        parser.onMessage(data.toString(), manager::handle);
        return WebSocket.Listener.super.onText(webSocket, data, last);
      }

      @Override
      public CompletionStage<?> onClose(WebSocket webSocket, int statusCode, String reason) {
        manager.handle(parser.onClose(statusCode, reason));
        return WebSocket.Listener.super.onClose(webSocket, statusCode, reason);
      }
    }).thenAcceptAsync(ws -> this.ws = ws).getNow(null);
  }

  @Override
  public final void stop(int code, @Nullable String reason) {
    if (ws == null) throw new NotYetConnectedException();
    this.ws.sendClose(code, (reason == null) ? "" : reason)
      .thenRunAsync(() -> this.ws = null)
      .getNow(null);
  }

  @Override
  public final void sendRaw(String message) {
    if (ws == null) throw new NotYetConnectedException();
    this.ws.sendText(message, true).getNow(this.ws);
  }
}
