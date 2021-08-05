module com.github.twitch4j.clients.http {
  requires com.github.twitch4j.clients.mapper;
  requires reactor.core;
  requires io.reactivex.rxjava3;
  requires org.apache.commons.collections4;
  requires org.apache.commons.text;
  requires transitive kotlin.stdlib.jdk8;

  requires static org.jetbrains.annotations;
  requires static lombok;

  requires transitive java.net.http;

  exports com.github.twitch4j.clients.http;
  exports com.github.twitch4j.clients.http.apache;
  exports com.github.twitch4j.clients.http.jdk11;
  exports com.github.twitch4j.clients.http.okhttp;
  exports com.github.twitch4j.clients.http.rest;
}
