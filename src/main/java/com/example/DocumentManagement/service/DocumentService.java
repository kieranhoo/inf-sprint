package com.example.DocumentManagement.service;

import com.example.DocumentManagement.entity.DocumentEntity;
import com.example.DocumentManagement.repository.DocumentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DocumentService {
    private final DocumentRepository documentRepository;

    public String helloWorld() {
        return "Hello world";
    }

    public String updateLoadDocument(String url) {
        documentRepository.save(new DocumentEntity(url, null, false,null));

        return "Upload SuccessFully!";
    }
    public List<DocumentEntity> getAllDocuments(){
        return documentRepository.findAll();
    }
}
