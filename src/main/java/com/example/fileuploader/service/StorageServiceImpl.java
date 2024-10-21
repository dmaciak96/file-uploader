package com.example.fileuploader.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.InputStream;

@Slf4j
@Service
public class StorageServiceImpl implements StorageService {

    @Override
    public void store(String fileName, InputStream content) {
        log.info("Saving file {}", fileName);
    }
}
