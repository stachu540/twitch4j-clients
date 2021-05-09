module com.github.twitch4j.clients.mapper {
  requires com.fasterxml.jackson.databind;
  requires com.fasterxml.jackson.core;
  requires com.fasterxml.jackson.annotation;
  requires com.google.gson;

  requires java.base;
  requires org.apache.commons.io;

  requires static org.jetbrains.annotations;
  requires static lombok;

  exports com.github.twitch4j.clients.mapper;
  exports com.github.twitch4j.clients.mapper.gson;
  exports com.github.twitch4j.clients.mapper.jackson;
}
