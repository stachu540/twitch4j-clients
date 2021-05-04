package com.github.twitch4j.clients.http;

import org.apache.commons.collections4.MultiValuedMap;
import com.github.twitch4j.clients.mapper.IBody;

public interface IMessage {
    MultiValuedMap<String, String> getHeaders();

    IBody getBody();
}
