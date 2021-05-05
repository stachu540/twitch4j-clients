package com.github.twitch4j.clients.mapper;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import org.jetbrains.annotations.Nullable;

public interface IBody {
  @Nullable
  Charset getCharset();

  int getSize();

  InputStream getAsStream();

  ByteBuffer getAsBuffer();

  Reader getAsReader();

  String getAsString() throws IOException;

  byte[] getAsBytes() throws IOException;

  <T> T getAs(Class<T> type) throws IOException;
}
