package com.example.fileuploader.repository;

import com.example.fileuploader.model.FileMetadata;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface FileMetadataRepository extends R2dbcRepository<FileMetadata, UUID> {
}
