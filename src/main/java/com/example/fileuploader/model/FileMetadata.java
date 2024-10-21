package com.example.fileuploader.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;

import java.util.UUID;

@Data
@Builder
public class FileMetadata {

    @Id
    private UUID id;
    private String filename;
    private String digest;
    private long fileSizeInBytes;
}
