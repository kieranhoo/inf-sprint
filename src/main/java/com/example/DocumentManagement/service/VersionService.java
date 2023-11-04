package com.example.DocumentManagement.service;

import com.example.DocumentManagement.entity.VersionEntity;
import com.example.DocumentManagement.repository.VersionRepository;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VersionService {
    private final VersionRepository versionRepository;

    public String helloWorld() {
        return "Hello world, this is Service";
    }

    public List<VersionEntity> getAllVersions(Long documentId)   
    {  
        List<VersionEntity> versions = new ArrayList<VersionEntity>();  
        versionRepository.findByDocumentId(documentId).forEach(books1 -> versions.add(books1));  
        return versions;  
    } 
}
