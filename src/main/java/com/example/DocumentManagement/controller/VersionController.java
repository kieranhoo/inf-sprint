package com.example.DocumentManagement.controller;

import com.example.DocumentManagement.entity.DocumentEntity;
import com.example.DocumentManagement.entity.VersionEntity;
import com.example.DocumentManagement.request.UrlUploadRequest;
import com.example.DocumentManagement.service.VersionService;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/document")
@RequiredArgsConstructor
public class VersionController {
    @Autowired
    private VersionService versionService;

    @GetMapping("/{documentId}/version")
    public ResponseEntity<List<VersionEntity>> getDocumentVersions(@PathVariable("documentId") Long documentId) {
        return ResponseEntity.ok(versionService.getAllVersions(documentId));
    }
}
