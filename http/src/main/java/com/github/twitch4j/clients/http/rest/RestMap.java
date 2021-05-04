package com.github.twitch4j.clients.http.rest;

import java.io.IOException;
import java.util.function.Consumer;
import java.util.function.Function;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public final class RestMap<T, R> extends AbstractRest<R> {
    private final Rest<T> base;
    private final Function<T, R> transformer;

    @Override
    public R get() throws Exception {
        try {
            R result = transformer.apply(base.get());
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
    public void enqueue(Consumer<R> response, Consumer<Throwable> error) {
        try {
            base.enqueue(result -> response.andThen(this::doSuccess).accept(transformer.apply(result)), error.andThen(this::doError));
        } finally {
            doCompleted();
        }
    }
}
