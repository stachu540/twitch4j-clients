package io.github.twitch4j.http.client.jdk11;

import com.github.twitch4j.clients.http.ICall;
import com.github.twitch4j.clients.http.Request;
import com.github.twitch4j.clients.http.Response;
import com.github.twitch4j.clients.http.Status;
import com.github.twitch4j.clients.mapper.IMapper;
import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Objects;
import java.util.function.Consumer;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.MultiMapUtils;
import org.apache.commons.collections4.MultiValuedMap;

@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public final class JavaCall implements ICall {
  @Getter
  private final Request request;
  private final IMapper mapper;
  private final HttpClient client;

  @Override
  public final Response execute() throws Exception {
    return toResponse(client.send(toRequest(), HttpResponse.BodyHandlers.ofByteArray()));
  }

  @Override
  public final void enqueue(Consumer<Response> response, Consumer<Throwable> error) {
    try {
      response.accept(execute());
    } catch (Exception e) {
      error.accept(e);
    }
  }

  private Response toResponse(HttpResponse<byte[]> realResponse) throws IOException {
    MultiValuedMap<String, String> headers = MultiMapUtils.newListValuedHashMap();
    realResponse.headers().map().forEach(headers::putAll);
    return new Response(request, Objects.requireNonNull(Status.ofCode(realResponse.statusCode())), mapper.mapTo(realResponse.body()), MultiMapUtils.unmodifiableMultiValuedMap(headers));
  }

  private HttpRequest toRequest() {
    HttpRequest.Builder builder = HttpRequest.newBuilder(request.getUrl())
      .method(request.getMethod().toString(), HttpRequest.BodyPublishers.ofInputStream(() -> request.getBody().getAsStream()));

    request.getHeaders().entries().forEach(e -> builder.header(e.getKey(), e.getValue()));

    return builder.build();
  }
}
