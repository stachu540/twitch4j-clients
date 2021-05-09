open module com.github.twitch4j.clients.event {
  requires reactor.core;
  requires io.reactivex.rxjava3;
  requires transitive kotlin.stdlib.jdk8;

  requires java.base;

  requires static lombok;

  exports com.github.twitch4j.clients.event;
}
