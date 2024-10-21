package com.example.fileuploader.service;

import com.example.fileuploader.exception.FileMetadataException;
import com.example.fileuploader.model.FileMetadata;
import com.example.fileuploader.repository.FileMetadataRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.atomic.AtomicLong;

@Slf4j
@Service
@RequiredArgsConstructor
public class FileMetadataServiceImpl implements FileMetadataService {
    public static final String DIGEST_ALGORITHM = "SHA-256";

    private final FileMetadataRepository fileMetadataRepository;
    private final StorageService storageService;

    @Override
    public Flux<FileMetadata> saveAll(Flux<FilePart> fileParts) {
        return fileParts
                .flatMap(this::saveFileMetadata)
                .flatMap(fileMetadataRepository::save);
    }

    private Mono<FileMetadata> saveFileMetadata(FilePart filePart) {
        try {
            var totalBytesRead = new AtomicLong(0);
            var messageDigest = MessageDigest.getInstance(DIGEST_ALGORITHM);
            return filePart.content()
                    .flatMap(dataBuffer -> {
                        var bytes = new byte[dataBuffer.readableByteCount()];
                        dataBuffer.read(bytes);
                        messageDigest.update(bytes);
                        totalBytesRead.addAndGet(bytes.length);
                        storageService.store(filePart.filename(), dataBuffer.asInputStream());
                        return Mono.just(dataBuffer);
                    })
                    .then(Mono.fromCallable(() -> FileMetadata.builder()
                            .fileSizeInBytes(totalBytesRead.get())
                            .digest(DigestUtils.md5DigestAsHex(messageDigest.digest()))
                            .filename(filePart.filename())
                            .build()));
        } catch (NoSuchAlgorithmException e) {
            return Mono.error(new FileMetadataException("File processing exception: " + e.getMessage()));
        }
    }

    @Override
    public Flux<FileMetadata> findAll() {
        return fileMetadataRepository.findAll();
    }
}
