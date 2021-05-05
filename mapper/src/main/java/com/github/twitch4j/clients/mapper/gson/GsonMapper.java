package com.github.twitch4j.clients.mapper.gson;

import com.github.twitch4j.clients.mapper.IBody;
import com.github.twitch4j.clients.mapper.IMapper;
import com.github.twitch4j.clients.mapper.impl.BodyImpl;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.IOUtils;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class GsonMapper implements IMapper {
  private final Gson mapper;

  public static IMapper create() {
    return create(new Gson());
  }

  public static IMapper create(Gson gson) {
    return new GsonMapper(gson);
  }

  public static IMapper create(GsonBuilder builder) {
    return create(builder.create());
  }

  @Override
  public final <T> T mapFrom(IBody body, Class<T> type) throws IOException {
    try {
      return mapper.fromJson(body.getAsReader(), type);
    } catch (JsonParseException e) {
      throw new IOException(e);
    }
  }

  @Override
  public final IBody mapTo(Object body) {
    return mapTo(body, Charset.defaultCharset());
  }

  @Override
  public final IBody mapTo(Object body, Charset charset) {
    String raw = mapper.toJson(body);
    InputStream data = IOUtils.toInputStream(raw, charset);
    return new BodyImpl(data, this, charset, raw.length());
  }
}
