package com.github.twitch4j.clients.http;

import com.github.twitch4j.clients.mapper.IBody;
import java.net.URI;
import lombok.Value;
import org.apache.commons.collections4.MultiValuedMap;
import org.jetbrains.annotations.NotNull;

@Value
public class Request implements IMessage {
    @NotNull
    HttpMethod method;
    @NotNull
    URI url;
    @NotNull
    IBody body;
    @NotNull
    MultiValuedMap<String, String> headers;
}
