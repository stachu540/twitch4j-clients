package com.github.twitch4j.clients.mapper.jackson;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.github.twitch4j.clients.mapper.IBody;
import com.github.twitch4j.clients.mapper.IMapper;
import com.github.twitch4j.clients.mapper.impl.BodyImpl;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class JacksonMapper implements IMapper {
    private final ObjectMapper mapper;

    @Override
    public final <T> T mapFrom(IBody body, Class<T> type) throws IOException {
        return mapper.readValue(body.getAsReader(), type);
    }

    @Override
    public final IBody mapTo(Object body) throws IOException {
        return mapTo(body, Charset.defaultCharset());
    }

    @Override
    public final IBody mapTo(Object body, Charset charset) throws IOException {
        byte[] raw = mapper.writeValueAsBytes(body);
        InputStream data = new ByteArrayInputStream(raw);
        return new BodyImpl(data, this, charset, raw.length);
    }

    public static IMapper create() {
        return create(new ObjectMapper());
    }

    public static IMapper create(ObjectMapper gson) {
        return new JacksonMapper(gson);
    }

    public static IMapper create(JsonMapper.Builder builder) {
        return create(builder.build());
    }
}
