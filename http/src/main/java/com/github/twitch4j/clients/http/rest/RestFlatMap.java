package com.github.twitch4j.clients.http.rest;

import java.io.IOException;
import java.util.function.Consumer;
import java.util.function.Function;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public final class RestFlatMap<T, R> extends AbstractRest<R> {
    private final Rest<T> base;
    private final Function<T, Rest<R>> transformer;

    @Override
    public final R get() throws Exception {
        try {
            R result = transformer.apply(base.get()).get();
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
    public final void enqueue(Consumer<R> response, Consumer<Throwable> error) {
        try {
            base.enqueue(result -> transformer.apply(result).enqueue(response.andThen(this::doSuccess), error.andThen(this::doError)), error.andThen(this::doError));
        } finally {
            doCompleted();
        }
    }
}
