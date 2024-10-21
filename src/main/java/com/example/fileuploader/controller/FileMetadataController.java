package com.example.fileuploader.controller;

import com.example.fileuploader.model.FileMetadata;
import com.example.fileuploader.service.FileMetadataService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/metadata")
public class FileMetadataController {

    private final FileMetadataService fileMetadataService;

    @PostMapping
    public Flux<FileMetadata> saveSingleFileMetadata(@RequestPart("files") Flux<FilePart> fileParts) {
        return fileMetadataService.saveAll(fileParts);
    }

    @GetMapping
    public Flux<FileMetadata> findAll() {
        return fileMetadataService.findAll();
    }
}
