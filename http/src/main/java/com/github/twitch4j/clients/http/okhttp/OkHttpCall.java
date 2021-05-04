package com.github.twitch4j.clients.http.okhttp;

import com.github.twitch4j.clients.http.ICall;
import com.github.twitch4j.clients.http.Request;
import com.github.twitch4j.clients.http.Response;
import com.github.twitch4j.clients.http.Status;
import com.github.twitch4j.clients.mapper.IMapper;
import java.io.IOException;
import java.util.function.Consumer;
import lombok.Getter;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import okhttp3.internal.http.HttpMethod;
import org.apache.commons.collections4.MultiMapUtils;
import org.apache.commons.collections4.MultiValuedMap;
import org.jetbrains.annotations.NotNull;


public final class OkHttpCall implements ICall {
    @Getter
    private final Request request;
    private final IMapper mapper;
    private final OkHttpClient client;

    OkHttpCall(Request request, IMapper mapper, OkHttpClient client) {
        this.request = request;
        this.mapper = mapper;
        this.client = client;
    }

    @Override
    public final Response execute() throws Exception {
        return doResponse(client.newCall(doRequest()).execute());
    }

    @Override
    public final void enqueue(Consumer<Response> result, Consumer<Throwable> error) {
        try {
            client.newCall(doRequest()).enqueue(new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    error.accept(e);
                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull okhttp3.Response response) throws IOException {
                    result.accept(doResponse(response));
                }
            });
        } catch (IOException e) {
            error.accept(e);
        }
    }

    private okhttp3.Request doRequest() throws IOException {
        RequestBody body = (HttpMethod.requiresRequestBody(request.getMethod().name()) && request.getBody().getSize() == 0) ? RequestBody.create(new byte[0]) : null;

        if (request.getBody().getSize() > 0 && HttpMethod.permitsRequestBody(request.getMethod().name())) {
            body = RequestBody.create(request.getBody().getAsBytes());
        }

        okhttp3.Request.Builder builder = new okhttp3.Request.Builder().url(HttpUrl.get(request.getUrl()))
            .method(request.getMethod().name(), body);

        request.getHeaders().entries().forEach(e -> builder.addHeader(e.getKey(), e.getValue()));

        return builder.build();
    }

    private Response doResponse(okhttp3.Response realResponse) throws IOException {
        MultiValuedMap<String, String> headers = MultiMapUtils.newListValuedHashMap();
        Object body = realResponse.body();
        if (body != null) {
            body = ((ResponseBody) body).byteStream();
        }
        realResponse.headers().toMultimap().forEach(headers::putAll);

        return new Response(request, Status.ofCode(realResponse.code()), mapper.mapTo(body), MultiMapUtils.unmodifiableMultiValuedMap(headers));
    }

}
