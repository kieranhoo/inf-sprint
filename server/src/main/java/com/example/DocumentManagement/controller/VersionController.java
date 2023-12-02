package com.example.DocumentManagement.controller;

import com.example.DocumentManagement.entity.VersionEntity;
import com.example.DocumentManagement.response.ListResponse;
import com.example.DocumentManagement.response.MessageResponse;
import com.example.DocumentManagement.service.VersionService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class VersionController {
    @Autowired
    private VersionService versionService;

    @GetMapping("{document-id}/version")
    public ResponseEntity<ListResponse> getDocumentVersions(
            @PathVariable(name = "document-id") String documentId
    ) {
        return ResponseEntity.ok(versionService.getAllVersions(documentId));
    }

    @GetMapping("/version/{id}")
    public ResponseEntity<VersionEntity> getOneVersion(
            @PathVariable(name = "id") String id
    ){
        return ResponseEntity.ok(versionService.getOneVersion(id));
    }

    @GetMapping("/version/{id}/latest-version")
    public ResponseEntity<MessageResponse> getLatestVersion(
            @PathVariable(name = "id") String id
    ){
        return ResponseEntity.ok(versionService.getLatestVersionByDocumentId(id));
    }
}
