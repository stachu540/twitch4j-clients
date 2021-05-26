package io.github.twitch4j.http.client.jdk11;

import com.github.twitch4j.clients.http.HttpEngine;
import com.github.twitch4j.clients.http.ICall;
import com.github.twitch4j.clients.http.Request;
import com.github.twitch4j.clients.mapper.IMapper;
import java.net.http.HttpClient;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public final class JavaHttpEngine implements HttpEngine {
  private final HttpClient client;

  public static HttpEngine create() {
    return create(HttpClient.newHttpClient());
  }

  public static HttpEngine create(HttpClient client) {
    return new JavaHttpEngine(client);
  }

  @Override
  public final ICall create(Request request, IMapper mapper) {
    return new JavaCall(request, mapper, client);
  }
}
