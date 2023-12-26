package com.nix.ecommerceapi.controller;

import com.nix.ecommerceapi.exception.BadRequestException;
import com.nix.ecommerceapi.service.StorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class UploadController {
    private final StorageService storageService;

    @PostMapping("/upload")
    @PreAuthorize("hasAnyRole('ADMIN', 'SHOP')")
    public ResponseEntity<Map<?, ?>> upload(@RequestParam("file") MultipartFile file,
                                            @RequestParam("folder") String folder) throws IOException {
        if (file.isEmpty()) {
            throw new BadRequestException("File is empty");
        }
        Map<?, ?> result = storageService.uploadFile(file.getInputStream(), folder);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/file/{fileId}")
    public ResponseEntity<Map<?, ?>> getFileInfo(@PathVariable String fileId) {
        String fileUrl = storageService.getFileUrl(fileId);
        return ResponseEntity.ok(Map.of("url", fileUrl));
    }

    @DeleteMapping("/file/{public_id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'SHOP')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteFile(@PathVariable String public_id) {
        storageService.deleteFile(public_id);
    }
}

