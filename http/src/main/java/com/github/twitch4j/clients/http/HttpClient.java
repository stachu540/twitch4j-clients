package com.github.twitch4j.clients.http;

import com.github.twitch4j.clients.http.rest.Rest;
import com.github.twitch4j.clients.http.rest.RestImpl;
import com.github.twitch4j.clients.mapper.IMapper;
import java.net.URI;
import java.util.function.Consumer;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public final class HttpClient {
    private final String baseUrl;
    @Getter
    private final IMapper mapper;
    private final HttpEngine engine;

    public URI getBaseUrl() {
        return URI.create(baseUrl);
    }

    private <T> Rest<T> create(HttpMethod method, String endpoint, Class<T> type, Consumer<RequestSpec> spec) {
        RequestSpec spec1 = new RequestSpec(mapper, method, baseUrl, endpoint);
        spec.accept(spec1);
        return new RestImpl<>(engine.create(spec1.create(), mapper), type);
    }

    public final <T> Rest<T> get(String endpoint, Class<T> type) {
        return get(endpoint, type, spec -> {
        });
    }

    public final <T> Rest<T> get(String endpoint, Class<T> type, Consumer<RequestSpec> spec) {
        return create(HttpMethod.GET, endpoint, type, spec);
    }

    public final <T> Rest<T> post(String endpoint, Class<T> type) {
        return post(endpoint, type, spec -> {
        });
    }

    public final <T> Rest<T> post(String endpoint, Class<T> type, Consumer<RequestSpec> spec) {
        return create(HttpMethod.POST, endpoint, type, spec);
    }

    public final <T> Rest<T> put(String endpoint, Class<T> type) {
        return put(endpoint, type, spec -> {
        });
    }

    public final <T> Rest<T> put(String endpoint, Class<T> type, Consumer<RequestSpec> spec) {
        return create(HttpMethod.PUT, endpoint, type, spec);
    }

    public final <T> Rest<T> patch(String endpoint, Class<T> type) {
        return patch(endpoint, type, spec -> {
        });
    }

    public final <T> Rest<T> patch(String endpoint, Class<T> type, Consumer<RequestSpec> spec) {
        return create(HttpMethod.PATCH, endpoint, type, spec);
    }

    public final <T> Rest<T> delete(String endpoint, Class<T> type) {
        return delete(endpoint, type, spec -> {
        });
    }

    public final <T> Rest<T> delete(String endpoint, Class<T> type, Consumer<RequestSpec> spec) {
        return create(HttpMethod.DELETE, endpoint, type, spec);
    }

    public final <T> Rest<T> options(String endpoint, Class<T> type) {
        return options(endpoint, type, spec -> {
        });
    }

    public final <T> Rest<T> options(String endpoint, Class<T> type, Consumer<RequestSpec> spec) {
        return create(HttpMethod.OPTIONS, endpoint, type, spec);
    }
}
