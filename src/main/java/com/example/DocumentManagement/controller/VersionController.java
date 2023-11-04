package com.example.DocumentManagement.controller;

import com.example.DocumentManagement.entity.DocumentEntity;
import com.example.DocumentManagement.entity.VersionEntity;
import com.example.DocumentManagement.request.UrlUploadRequest;
import com.example.DocumentManagement.service.VersionService;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/document")
@RequiredArgsConstructor
public class VersionController {
    @Autowired
    private VersionService versionService;

    @GetMapping(value = "/{documentId}/version", produces = MimeTypeUtils.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<VersionEntity>> getDocumentVersions(@PathVariable("documentId") Long documentId) {
        return ResponseEntity.ok(versionService.getAllVersions(documentId));
    }

    @GetMapping(value = "/{documentId}/version/{versionId}", produces = MimeTypeUtils.APPLICATION_JSON_VALUE)
    public ResponseEntity<VersionEntity> getDocumentVersionById(@PathVariable("documentId") Long documentId, @PathVariable("versionId") Long versionId) {
        return ResponseEntity.ok(versionService.getVersionById(documentId, versionId));
    }
}
