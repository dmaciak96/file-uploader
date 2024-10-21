package com.example.fileuploader.service;

import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.codec.multipart.FilePart;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.file.Path;

public record FilePartMock(String filename, Flux<DataBuffer> content) implements FilePart {

    @Override
    public Mono<Void> transferTo(Path dest) {
        return Mono.empty();
    }

    @Override
    public String name() {
        return "file";
    }

    @Override
    public HttpHeaders headers() {
        return null;
    }
}
