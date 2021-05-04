package com.github.twitch4j.clients.mapper.impl;

import com.github.twitch4j.clients.mapper.IBody;
import com.github.twitch4j.clients.mapper.IMapper;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.IOUtils;
import org.jetbrains.annotations.Nullable;

@RequiredArgsConstructor
public class BodyImpl implements IBody {
    private final InputStream data;
    private final IMapper mapper;
    @Getter
    @Nullable
    private final Charset charset;
    @Getter
    private final int size;

    @Override
    public InputStream getAsStream() {
        return IOUtils.buffer(data);
    }

    @Override
    public ByteBuffer getAsBuffer() {
        try {
            return ByteBuffer.wrap(getAsBytes());
        } catch (IOException e) {
            return ByteBuffer.allocate(size);
        }
    }

    @Override
    public Reader getAsReader() {
        return new InputStreamReader(data);
    }

    @Override
    public String getAsString() throws IOException {
        return IOUtils.toString(data, (charset == null) ? Charset.defaultCharset() : charset);
    }

    @Override
    public byte[] getAsBytes() throws IOException {
        return IOUtils.readFully(data, size);
    }

    @Override
    public <T> T getAs(Class<T> type) throws IOException {
        return mapper.mapFrom(this, type);
    }
}
