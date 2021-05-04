package com.github.twitch4j.clients.http.rest;

import io.reactivex.rxjava3.core.Single;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;
import java.util.function.Consumer;
import java.util.function.Function;
import reactor.core.publisher.Mono;

public abstract class AbstractRest<T> implements Rest<T> {
    protected final List<Runnable> completed = new ArrayList<>();
    protected final List<Consumer<T>> success = new ArrayList<>();
    protected final List<Consumer<Throwable>> error = new ArrayList<>();

    @Override
    public final <R> Rest<R> map(Function<T, R> transformer) {
        return new RestMap<>(this, transformer);
    }

    @Override
    public final <R> Rest<R> flatMap(Function<T, Rest<R>> transformer) {
        return new RestFlatMap<>(this, transformer);
    }

    @Override
    public final Future<T> getFuture() {
        CompletableFuture<T> cf = new CompletableFuture<>();
        enqueue(cf::complete, cf::completeExceptionally);
        return cf;
    }

    @Override
    public final Mono<T> getMono() {
        return Mono.create(sink -> enqueue(sink::success, sink::error));
    }

    @Override
    public final Single<T> getSingle() {
        return Single.create(sink -> enqueue(sink::onSuccess, sink::onError));
    }

    public final Rest<T> onSuccess(Consumer<T> result) {
        this.success.add(result);
        return this;
    }

    public final Rest<T> onError(Consumer<Throwable> error) {
        this.error.add(error);
        return this;
    }

    public final <X extends Throwable> Rest<T> onError(Class<X> type, Consumer<X> error) {
        return onError(e -> {
            if (type.isInstance(e)) {
                error.accept(type.cast(e));
            }
        });
    }

    public final Rest<T> onComplete(Runnable completed) {
        this.completed.add(completed);
        return this;
    }

    protected final void doCompleted() {
        completed.forEach(Runnable::run);
    }

    protected final void doError(final Throwable throwable) {
        error.forEach(e -> e.accept(throwable));
    }

    protected final void doSuccess(final T result) {
        success.forEach(r -> r.accept(result));
    }
}
