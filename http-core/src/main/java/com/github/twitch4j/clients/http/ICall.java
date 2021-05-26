package com.github.twitch4j.clients.http;

import java.util.function.Consumer;

public interface ICall {
  Request getRequest();

  Response execute() throws Exception;

  void enqueue(Consumer<Response> response, Consumer<Throwable> error);

  default void enqueue(Consumer<Response> response) {
    enqueue(response, ignore -> {
    });
  }

  default void enqueue() {
    enqueue(ignore -> {
    });
  }
}
