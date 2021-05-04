package com.github.twitch4j.clients.websocket;

import com.github.twitch4j.clients.websocket.event.Event;
import java.util.function.Consumer;

public interface WebSocketMapper {
    void map(String message, Consumer<Event> event);
}
