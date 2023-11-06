package com.example.DocumentManagement.controller;

import com.example.DocumentManagement.response.ListResponse;
import com.example.DocumentManagement.service.VersionService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/version")
@RequiredArgsConstructor
public class VersionController {
    @Autowired
    private VersionService versionService;

    @GetMapping("{document-id}")
    public ResponseEntity<ListResponse> getDocumentVersions(
            @PathVariable(name = "document-id") String documentId
    ) {
        return ResponseEntity.ok(versionService.getAllVersions(documentId));
    }
}
