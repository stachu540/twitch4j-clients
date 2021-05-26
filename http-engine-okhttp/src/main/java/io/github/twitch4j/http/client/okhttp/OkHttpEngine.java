package io.github.twitch4j.http.client.okhttp;

import com.github.twitch4j.clients.http.HttpEngine;
import com.github.twitch4j.clients.http.ICall;
import com.github.twitch4j.clients.http.Request;
import com.github.twitch4j.clients.mapper.IMapper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import okhttp3.OkHttpClient;


@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public final class OkHttpEngine implements HttpEngine {
  private final OkHttpClient client;

  public static HttpEngine create() {
    return create(new OkHttpClient());
  }

  public static HttpEngine create(OkHttpClient client) {
    return new OkHttpEngine(client);
  }

  @Override
  public final ICall create(Request request, IMapper mapper) {
    return new OkHttpCall(request, mapper, client);
  }
}
