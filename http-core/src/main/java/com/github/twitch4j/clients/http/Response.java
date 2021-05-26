package com.github.twitch4j.clients.http;

import com.github.twitch4j.clients.mapper.IBody;
import lombok.Value;
import org.apache.commons.collections4.MultiValuedMap;
import org.jetbrains.annotations.NotNull;

@Value
public class Response implements IMessage {
  @NotNull
  Request request;
  @NotNull
  Status status;
  @NotNull
  IBody body;
  @NotNull
  MultiValuedMap<String, String> headers;
}
