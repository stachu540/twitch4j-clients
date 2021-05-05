package com.github.twitch4j.clients.mapper;

import java.io.IOException;
import java.nio.charset.Charset;

public interface IMapper {
  <T> T mapFrom(IBody body, Class<T> type) throws IOException;

  IBody mapTo(Object body) throws IOException;

  IBody mapTo(Object body, Charset charset) throws IOException;
}
