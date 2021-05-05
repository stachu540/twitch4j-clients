package com.github.twitch4j.clients.http.apache;

import com.github.twitch4j.clients.http.HttpEngine;
import com.github.twitch4j.clients.http.ICall;
import com.github.twitch4j.clients.http.Request;
import com.github.twitch4j.clients.mapper.IMapper;
import lombok.RequiredArgsConstructor;
import org.apache.hc.client5.http.classic.HttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;

@RequiredArgsConstructor
public class ApacheHttpEngine implements HttpEngine {
  private final HttpClient client;

  public static HttpEngine create() {
    return create(HttpClientBuilder.create().build());
  }

  public static HttpEngine create(HttpClient client) {
    return new ApacheHttpEngine(client);
  }

  @Override
  public ICall create(Request request, IMapper mapper) {
    return new ApacheCall(request, mapper, client);
  }
}
