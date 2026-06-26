package com.restclient.service;

import com.restclient.core.model.Request;
import com.restclient.core.model.Response;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.concurrent.TimeUnit;

/**
 * Executes HTTP requests using OkHttp and maps the result to the domain {@link Response}.
 *
 * <p>The {@link OkHttpClient} instance is shared and thread-safe; OkHttp manages its own
 * connection pool internally. Callers should invoke {@link #execute(Request)} from a
 * non-FX thread and dispatch UI updates via {@code Platform.runLater()}.
 */
@Service
public class HttpExecutionService {

    private final OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build();

    /**
     * Sends the given request and blocks until a response arrives or an error occurs.
     *
     * @param domainRequest the request to execute
     * @return the HTTP response
     * @throws IOException on any network or I/O error
     */
    public Response execute(Request domainRequest) throws IOException {
        var okRequest = buildOkRequest(domainRequest);
        var start = System.currentTimeMillis();
        try (var okResponse = client.newCall(okRequest).execute()) {
            return buildDomainResponse(domainRequest.getId(), okResponse, System.currentTimeMillis() - start);
        }
    }

    private okhttp3.Request buildOkRequest(Request r) {
        var builder = new okhttp3.Request.Builder().url(r.getUrl());
        addEnabledHeaders(builder, r);
        applyMethod(builder, r);
        return builder.build();
    }

    private void addEnabledHeaders(okhttp3.Request.Builder builder, Request r) {
        if (r.getHeaders() == null) return;
        r.getHeaders().stream()
                .filter(h -> h.isEnabled() && h.getKey() != null && !h.getKey().isBlank())
                .forEach(h -> builder.addHeader(h.getKey(), h.getValue() != null ? h.getValue() : ""));
    }

    private void applyMethod(okhttp3.Request.Builder builder, Request r) {
        var body = buildOkBody(r);
        switch (r.getMethod()) {
            case GET -> builder.get();
            case POST -> builder.post(body != null ? body : emptyBody());
            case PUT -> builder.put(body != null ? body : emptyBody());
            case PATCH -> builder.patch(body != null ? body : emptyBody());
            case DELETE -> { if (body != null) builder.delete(body); else builder.delete(); }
            case HEAD -> builder.head();
            case OPTIONS -> builder.method("OPTIONS", null);
        }
    }

    private okhttp3.RequestBody buildOkBody(Request r) {
        var domainBody = r.getBody();
        if (domainBody == null || domainBody.getContent() == null || domainBody.getContent().isBlank()) {
            return null;
        }
        var mediaType = MediaType.parse(
                domainBody.getContentType() != null ? domainBody.getContentType() : "text/plain");
        return okhttp3.RequestBody.create(domainBody.getContent(), mediaType);
    }

    private okhttp3.RequestBody emptyBody() {
        return okhttp3.RequestBody.create("", MediaType.parse("text/plain"));
    }

    private Response buildDomainResponse(String requestId, okhttp3.Response ok, long durationMs) throws IOException {
        var response = new Response();
        response.setRequestId(requestId);
        response.setStatusCode(ok.code());
        response.setStatusText(ok.message());
        response.setDurationMillis(durationMs);
        response.setContentType(ok.header("Content-Type"));
        var headers = new LinkedHashMap<String, String>();
        ok.headers().forEach(pair -> headers.put(pair.getFirst(), pair.getSecond()));
        response.setHeaders(headers);
        var okBody = ok.body();
        if (okBody != null) {
            var bytes = okBody.bytes();
            response.setSizeBytes(bytes.length);
            response.setBody(new String(bytes, StandardCharsets.UTF_8));
        }
        return response;
    }
}
