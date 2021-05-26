package com.github.twitch4j.clients.http;

import com.github.twitch4j.clients.mapper.IBody;
import com.github.twitch4j.clients.mapper.IMapper;
import com.github.twitch4j.clients.mapper.impl.BodyImpl;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URI;
import java.nio.charset.Charset;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.MultiMapUtils;
import org.apache.commons.collections4.SetValuedMap;
import org.apache.commons.text.StringSubstitutor;
import org.jetbrains.annotations.Nullable;

@RequiredArgsConstructor
public final class RequestSpec {
  private final IMapper mapper;
  private final HttpMethod method;
  private final String baseUrl;
  private final String endpoint;
  private final SetValuedMap<String, String> queryParameters = MultiMapUtils.newSetValuedHashMap();
  private final SetValuedMap<String, String> headers = MultiMapUtils.newSetValuedHashMap();
  private final Map<String, String> pathParameters = new LinkedHashMap<>();
  @Nullable
  private IBody body = null;

  public final void setPathParameter(String key, String value) {
    pathParameters.put(key, value);
  }

  public final void setQueryParameter(String key, String value) {
    queryParameters.put(key, value);
  }

  public final void setHeader(String key, String value) {
    headers.put(key, value);
  }

  public final void addQueryParameter(String key, String value) {
    Set<String> values = new LinkedHashSet<>(queryParameters.get(key));
    values.add(value);

    queryParameters.putAll(key, values);
  }

  public final void addHeader(String key, String value) {
    Set<String> values = new LinkedHashSet<>(headers.get(key));
    values.add(value);

    headers.putAll(key, values);
  }

  public final void setBody(Object body) throws IOException {
    this.body = mapper.mapTo(body);
  }

  Request create() {
    if (body == null) {
      body = new BodyImpl(new ByteArrayInputStream(new byte[0]), mapper, Charset.defaultCharset(), 0);
    }
    return new Request(method, getUrl(), body, headers);
  }

  URI getUrl() {
    StringBuilder sb = new StringBuilder();

    sb.append(getBaseUrl());
    sb.append(queryParameters.entries().stream().map(e -> String.format("%s=%s", e.getKey(), e.getValue())).collect(Collectors.joining("&", "?", "")));

    return URI.create(sb.toString());
  }

  String getBaseUrl() {
    return baseUrl + StringSubstitutor.replace(endpoint, pathParameters, "{", "}");
  }
}
