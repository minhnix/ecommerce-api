package com.nix.ecommerceapi.service;

import java.io.InputStream;
import java.util.Map;

public interface StorageService {
    Map<?, ?> uploadFile(InputStream inputStream, String folder);
    String getFileUrl(String fileId);
    void deleteFile(String fileId);
}
