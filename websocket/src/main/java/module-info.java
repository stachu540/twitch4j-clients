module com.github.twitch4j.clients.websocket {
  requires com.github.twitch4j.clients.event;

  requires okhttp3;
  requires transitive java.net.http;

  requires static org.jetbrains.annotations;
  requires static lombok;

  exports com.github.twitch4j.clients.websocket;
  exports com.github.twitch4j.clients.websocket.jdk11;
  exports com.github.twitch4j.clients.websocket.okhttp;
}
