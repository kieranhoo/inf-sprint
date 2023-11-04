package com.example.DocumentManagement.service;

import com.example.DocumentManagement.entity.DocumentEntity;
import com.example.DocumentManagement.entity.VersionEntity;
import com.example.DocumentManagement.repository.DocumentRepository;
import com.example.DocumentManagement.repository.VersionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalTime;
import java.sql.Date;

@Service
@RequiredArgsConstructor
public class DocumentService {
    private final DocumentRepository documentRepository;
    private final VersionRepository versionRepository;

    public String helloWorld() {
        return "Hello world";
    }

    public String updateLoadDocument(String url) {
        documentRepository.save(new DocumentEntity(url, null, false,null));

        return "Upload SuccessFully!";
    }

    public String updateDocument(Integer documentID, String url, String name) {
        Boolean latestVersion = true;
        Date currentDate = new Date(System.currentTimeMillis());
        VersionEntity newVersion = new VersionEntity(documentID, url, name, latestVersion, currentDate);
        versionRepository.save(newVersion);
        return "Update Version SuccessFully!";
    }
}
