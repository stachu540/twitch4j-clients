package com.github.twitch4j.clients.websocket.okhttp;

import com.github.twitch4j.clients.event.EventManager;
import com.github.twitch4j.clients.websocket.EventParser;
import com.github.twitch4j.clients.websocket.WebSocketEngine;
import java.net.URI;
import java.nio.channels.AlreadyConnectedException;
import java.nio.channels.NotYetConnectedException;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class OkHttpEngine implements WebSocketEngine {
  private WebSocket ws;

  @Override
  public final void start(URI url, EventManager manager, EventParser parser) {
    if (ws != null) throw new AlreadyConnectedException();
    ws = new OkHttpClient().newWebSocket(
      new Request.Builder().url(HttpUrl.get(url)).build(),
      new WebSocketListener() {
        @Override
        public void onClosed(@NotNull WebSocket webSocket, int code, @NotNull String reason) {
          manager.handle(parser.onClose(code, (reason.isEmpty()) ? null : reason));
          OkHttpEngine.this.ws = null;
        }

        @Override
        public void onMessage(@NotNull WebSocket webSocket, @NotNull String text) {
          parser.onMessage(text, manager::handle);
        }

        @Override
        public void onOpen(@NotNull WebSocket webSocket, @NotNull Response response) {
          OkHttpEngine.this.ws = webSocket;
          manager.handle(parser.onOpen());
        }
      }
    );
  }

  @Override
  public final void stop(int code, @Nullable String reason) {
    if (ws == null) throw new NotYetConnectedException();

  }

  @Override
  public final void sendRaw(String message) {
    if (ws == null) throw new NotYetConnectedException();
    this.ws.send(message);
  }
}
