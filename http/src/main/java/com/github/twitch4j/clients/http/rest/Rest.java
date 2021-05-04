package com.github.twitch4j.clients.http.rest;

import io.reactivex.rxjava3.core.Single;
import java.io.IOException;
import java.util.concurrent.Future;
import java.util.function.Consumer;
import java.util.function.Function;
import reactor.core.publisher.Mono;

public interface Rest<T> {
    <R> Rest<R> map(Function<T, R> transformer);

    <R> Rest<R> flatMap(Function<T, Rest<R>> transformer);

    T get() throws Exception;

    Future<T> getFuture();

    Mono<T> getMono();

    Single<T> getSingle();

    void enqueue(Consumer<T> response, Consumer<Throwable> error);

    Rest<T> onSuccess(Consumer<T> result);

    Rest<T> onError(Consumer<Throwable> error);

    <X extends Throwable> Rest<T> onError(Class<X> type, Consumer<X> error);

    Rest<T> onComplete(Runnable completed);
}
