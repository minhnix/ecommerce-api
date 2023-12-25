package com.nix.ecommerceapi.service.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.api.exceptions.BadRequest;
import com.nix.ecommerceapi.exception.NotFoundException;
import com.nix.ecommerceapi.service.StorageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

@Component
public class CloudinaryStorageService implements StorageService {
    private final Cloudinary cloudinary;

    public CloudinaryStorageService(@Value("${upload.cloudinary.url}") String cloudinaryUrl) {
        this.cloudinary = new Cloudinary(cloudinaryUrl);
    }

    @Override
    public String uploadFile(InputStream inputStream, String folder) {
        try {
            Map<String, Object> params = new HashMap<>();
            if (folder != null) {
                params.put("folder", folder);
            }
            params.put("resource_type", "auto");
            File tempFile = createTempFile(inputStream);
            Map<?, ?> uploadResult = cloudinary.uploader().upload(tempFile, params);
            tempFile.delete();
            return getAssetId(uploadResult);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String getFileUrl(String fileId) {
        try {
            Map<?, ?> result = cloudinary.api().resourceByAssetID(fileId, Map.of());
            return getSecureUrl(result);
        } catch (BadRequest e) {
            throw new NotFoundException(e.getMessage());
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteFile(String fileId) {
        try {
            cloudinary.uploader().destroy(fileId, Map.of());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private File createTempFile(InputStream inputStream) throws IOException {
        File tempFile = File.createTempFile("temp", null);
        BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(tempFile));
        inputStream.transferTo(outputStream);
        return tempFile;
    }

    private String getAssetId(Map<?, ?> uploadResult) {
        return (String) uploadResult.get("asset_id");
    }

    private String getSecureUrl(Map<?, ?> resourceResult) {
        return (String) resourceResult.get("secure_url");
    }
}
