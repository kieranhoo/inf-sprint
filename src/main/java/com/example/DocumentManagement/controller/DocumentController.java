package com.example.DocumentManagement.controller;

import com.example.DocumentManagement.request.UpdateVersion;
import com.example.DocumentManagement.request.UrlUploadRequest;
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

    @PutMapping("")
    public ResponseEntity<String> uploadPDF(@RequestBody UrlUploadRequest uploadRequest){
        return ResponseEntity.ok(documentService.updateLoadDocument(uploadRequest.getUrl()));
    }

    @PutMapping("/{documentID}")
    public ResponseEntity<String> updateDocument(@PathVariable String documentID, @RequestBody UpdateVersion uploadRequest){
       return ResponseEntity.ok(documentService.updateDocument(null, documentID, documentID));
    }  
}