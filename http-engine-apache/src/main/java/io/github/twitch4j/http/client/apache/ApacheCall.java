package io.github.twitch4j.http.client.apache;

import com.github.twitch4j.clients.http.ICall;
import com.github.twitch4j.clients.http.Request;
import com.github.twitch4j.clients.http.Response;
import com.github.twitch4j.clients.http.Status;
import com.github.twitch4j.clients.mapper.IBody;
import com.github.twitch4j.clients.mapper.IMapper;
import java.util.function.Consumer;
import lombok.Getter;
import org.apache.commons.collections4.MultiMapUtils;
import org.apache.commons.collections4.MultiValuedMap;
import org.apache.commons.io.IOUtils;
import org.apache.hc.client5.http.classic.HttpClient;
import org.apache.hc.core5.http.ClassicHttpRequest;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.Header;
import org.apache.hc.core5.http.io.HttpClientResponseHandler;
import org.apache.hc.core5.http.io.entity.HttpEntities;
import org.apache.hc.core5.http.message.BasicClassicHttpRequest;

public class ApacheCall implements ICall {
    @Getter
    private final Request request;
    private final IMapper mapper;
    private final HttpClient client;

    public ApacheCall(Request request, IMapper mapper, HttpClient client) {
        this.request = request;
        this.mapper = mapper;
        this.client = client;
    }

    @Override
    public final Response execute() throws Exception {
        return client.execute(doRequest(), doResponse());
    }

    @Override
    public final void enqueue(Consumer<Response> response, Consumer<Throwable> error) {
        try {
            response.accept(execute());
        } catch (Exception e) {
            error.accept(e);
        }
    }

    private HttpClientResponseHandler<Response> doResponse() {
        return response -> {
            IBody body = mapper.mapTo(response.getEntity().getContent());
            MultiValuedMap<String, String> headers = MultiMapUtils.newListValuedHashMap();
            for (Header header : response.getHeaders()) {
                headers.put(header.getName(), header.getValue());
            }
            return new Response(request, Status.ofCode(response.getCode()), body, MultiMapUtils.unmodifiableMultiValuedMap(headers));
        };
    }

    private ClassicHttpRequest doRequest() {
        BasicClassicHttpRequest realRequest = new BasicClassicHttpRequest(request.getMethod().name(), request.getUrl());
        realRequest.setEntity(HttpEntities.create(stream -> IOUtils.copy(request.getBody().getAsStream(), stream), ContentType.APPLICATION_JSON));
        request.getHeaders().entries().forEach(e -> realRequest.addHeader(e.getKey(), e.getValue()));

        return realRequest;
    }
}
