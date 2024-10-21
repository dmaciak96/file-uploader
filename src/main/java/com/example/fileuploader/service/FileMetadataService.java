package com.example.fileuploader.service;

import com.example.fileuploader.model.FileMetadata;
import org.springframework.http.codec.multipart.FilePart;
import reactor.core.publisher.Flux;

public interface FileMetadataService {

    Flux<FileMetadata> saveAll(Flux<FilePart> fileParts);

    Flux<FileMetadata> findAll();
}
