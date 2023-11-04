package com.example.DocumentManagement.controller;

import com.example.DocumentManagement.request.UpdateDocumentRequest;
import com.example.DocumentManagement.service.DocumentService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/document")
@RequiredArgsConstructor
public class DocumentController {
    @Autowired
    private DocumentService documentService;

    @GetMapping("/hello-world")
    public ResponseEntity<String> helloWorld(){
        return ResponseEntity.ok(documentService.helloWorld());
    }

    @PostMapping("")
    public ResponseEntity<String> createDocument(@RequestBody UpdateDocumentRequest uploadRequest){
        uploadRequest.setNameVersion("1.0.0");
        return ResponseEntity.ok(documentService.createDocument(uploadRequest));
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateDocument(
            @PathVariable String id,
            @RequestBody UpdateDocumentRequest uploadRequest){
        return ResponseEntity.ok(documentService.updateLoadDocument(uploadRequest, id));
    }
    @GetMapping("")
    public  ResponseEntity<?> getAllDocuments(){
        return ResponseEntity.ok(documentService.getAllDocuments());
    }

    
}
