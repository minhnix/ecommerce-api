package com.nix.ecommerceapi.service;

import java.io.InputStream;

public interface StorageService {
    String uploadFile(InputStream inputStream, String folder);
    String getFileUrl(String fileId);
    void deleteFile(String fileId);
}
