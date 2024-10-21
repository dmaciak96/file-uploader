package com.example.fileuploader.service;

import com.example.fileuploader.model.FileMetadata;
import com.example.fileuploader.repository.FileMetadataRepository;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.util.DigestUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class FileMetadataServiceTest {
    private static final String FILE_NAME = "testfile.txt";
    private static final byte[] CONTENT = "Test content".getBytes(StandardCharsets.UTF_8);

    private final FileMetadataService fileMetadataService;
    private final FileMetadataRepository fileMetadataRepositoryMock;
    private final StorageService storageServiceMock;

    public FileMetadataServiceTest() {
        this.fileMetadataRepositoryMock = mock(FileMetadataRepository.class);
        this.storageServiceMock = mock(StorageService.class);
        this.fileMetadataService = new FileMetadataServiceImpl(fileMetadataRepositoryMock, storageServiceMock);
        when(fileMetadataRepositoryMock.save(any(FileMetadata.class))).thenAnswer(invocation -> {
            var metadata = (FileMetadata) invocation.getArgument(0);
            metadata.setId(UUID.randomUUID());
            return Mono.just(metadata);
        });
    }

    @Test
    public void whenFilePartWasSentThenShouldCalculateChecksumAndSize() {
        StepVerifier.create(fileMetadataService.saveAll(Flux.just(createFilePart())))
                .assertNext(fileMetadataResult -> {
                    assertEquals(CONTENT.length, fileMetadataResult.getFileSizeInBytes());
                    assertEquals(FILE_NAME, fileMetadataResult.getFilename());
                    assertEquals(getChecksum(), fileMetadataResult.getDigest());
                    verify(fileMetadataRepositoryMock).save(fileMetadataResult);
                    verify(storageServiceMock).store(eq(FILE_NAME), any(InputStream.class));
                })
                .verifyComplete();
    }

    private FilePart createFilePart() {
        DataBuffer dataBuffer = new DefaultDataBufferFactory().wrap(CONTENT);
        var contentFlux = Flux.just(dataBuffer);
        return new FilePartMock(FILE_NAME, contentFlux);
    }

    private String getChecksum() {
        try {
            var messageDigest = MessageDigest.getInstance(FileMetadataServiceImpl.DIGEST_ALGORITHM);
            messageDigest.update(CONTENT);
            return DigestUtils.md5DigestAsHex(messageDigest.digest());
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
