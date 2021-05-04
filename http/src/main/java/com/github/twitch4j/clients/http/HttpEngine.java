package com.github.twitch4j.clients.http;

import com.github.twitch4j.clients.mapper.IMapper;

public interface HttpEngine {
    ICall create(Request request, IMapper mapper);
}
