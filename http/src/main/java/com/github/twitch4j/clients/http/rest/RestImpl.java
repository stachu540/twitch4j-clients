package com.github.twitch4j.clients.http.rest;

import com.github.twitch4j.clients.http.ICall;
import java.io.IOException;
import java.util.function.Consumer;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public final class RestImpl<T> extends AbstractRest<T> {
  private final ICall call;
  private final Class<T> type;

  @Override
  public final T get() throws Exception {
    try {
      T result = call.execute().getBody().getAs(type);
      doSuccess(result);
      return result;
    } catch (Exception e) {
      doError(e);
      throw e;
    } finally {
      doCompleted();
    }
  }

  @Override
  public final void enqueue(Consumer<T> response, Consumer<Throwable> error) {
    try {
      call.enqueue(result -> {
        try {
          response.andThen(this::doSuccess).accept(result.getBody().getAs(type));
        } catch (IOException e) {
          error.andThen(this::doError).accept(e);
        }
      }, error.andThen(this::doError));
    } finally {
      doCompleted();
    }
  }
}
