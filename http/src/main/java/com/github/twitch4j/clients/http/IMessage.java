package com.github.twitch4j.clients.http;

import com.github.twitch4j.clients.mapper.IBody;
import org.apache.commons.collections4.MultiValuedMap;

public interface IMessage {
  MultiValuedMap<String, String> getHeaders();

  IBody getBody();
}
