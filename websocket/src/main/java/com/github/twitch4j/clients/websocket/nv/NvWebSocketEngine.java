package com.github.twitch4j.clients.websocket.nv;

import com.github.twitch4j.clients.event.EventManager;
import com.github.twitch4j.clients.websocket.EventParser;
import com.github.twitch4j.clients.websocket.WebSocketEngine;
import com.neovisionaries.ws.client.WebSocket;
import com.neovisionaries.ws.client.WebSocketAdapter;
import com.neovisionaries.ws.client.WebSocketFactory;
import com.neovisionaries.ws.client.WebSocketFrame;
import java.io.IOException;
import java.net.URI;
import java.nio.channels.AlreadyConnectedException;
import java.nio.channels.NotYetConnectedException;
import java.util.List;
import java.util.Map;
import org.jetbrains.annotations.Nullable;

public final class NvWebSocketEngine implements WebSocketEngine {
  private WebSocket ws;

  @Override
  public final void start(URI url, EventManager manager, EventParser parser) {
    if (ws != null && ws.isOpen()) throw new AlreadyConnectedException();
    try {
      new WebSocketFactory().createSocket(url)
        .addListener(new WebSocketAdapter() {
          @Override
          public void onConnected(WebSocket websocket, Map<String, List<String>> headers) throws Exception {
            manager.handle(parser.onOpen());
          }

          @Override
          public void onDisconnected(WebSocket websocket, WebSocketFrame serverCloseFrame, WebSocketFrame clientCloseFrame, boolean closedByServer) throws Exception {
            int code = (closedByServer) ? serverCloseFrame.getCloseCode() : clientCloseFrame.getCloseCode();
            String reason = (closedByServer) ? serverCloseFrame.getCloseReason() : clientCloseFrame.getCloseReason();
            manager.handle(parser.onClose(code, (reason.isEmpty()) ? null : reason));
          }

          @Override
          public void onTextMessage(WebSocket websocket, String text) throws Exception {
            parser.onMessage(text, manager::handle);
          }
        });
    } catch (IOException e) {
      throw new IllegalStateException(e);
    }
  }

  @Override
  public final void stop(int code, @Nullable String reason) {
    if (ws == null || !ws.isOpen()) throw new NotYetConnectedException();
    ws.sendClose(code, reason);
  }

  @Override
  public final void sendRaw(String message) {
    if (ws == null || !ws.isOpen()) throw new NotYetConnectedException();
    ws.sendText(message);
  }
}
