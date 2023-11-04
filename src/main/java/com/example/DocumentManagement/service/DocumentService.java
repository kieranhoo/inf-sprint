package com.example.DocumentManagement.service;

import com.example.DocumentManagement.entity.DocumentEntity;
import com.example.DocumentManagement.entity.VersionEntity;
import com.example.DocumentManagement.repository.DocumentRepository;
import com.example.DocumentManagement.repository.VersionRepository;
import com.example.DocumentManagement.request.UpdateDocumentRequest;
import com.example.DocumentManagement.supportFunction.SupportFunction;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Service
@RequiredArgsConstructor
public class DocumentService extends SupportFunction {
    private final DocumentRepository documentRepository;
    private final VersionRepository versionRepository;

    public String helloWorld() {
        return "Hello world";
    }

    public String createDocument(UpdateDocumentRequest updateDocumentRequest) {
        LocalDateTime currentDateTime = LocalDateTime.now();
        java.util.Date utilDate = Date.from(currentDateTime.atZone(ZoneId.systemDefault()).toInstant());

        DocumentEntity documentEntity = documentRepository.save(new DocumentEntity(
                updateDocumentRequest.getNameDocument(),
                new Date(utilDate.getTime()),
                false,
                null
        ));

        versionRepository.save(new VersionEntity(
                documentEntity.getId(),
                updateDocumentRequest.getUrl(),
                updateDocumentRequest.getNameVersion(),
                true,
                new Date(utilDate.getTime())
        ));

        return "Upload SuccessFully!";
    }

    public String updateLoadDocument(UpdateDocumentRequest updateDocumentRequest, String idFromPathVariable) {
        int id = checkRequest(idFromPathVariable);

        LocalDateTime currentDateTime = LocalDateTime.now();
        java.util.Date utilDate = Date.from(currentDateTime.atZone(ZoneId.systemDefault()).toInstant());

        documentRepository.save(new DocumentEntity(
                updateDocumentRequest.getNameDocument(),
                new Date(utilDate.getTime()),
                false,
                null
        ));

        versionRepository.save(new VersionEntity(
                id,
                updateDocumentRequest.getUrl(),
                updateDocumentRequest.getNameVersion(),
                true,
                new Date(utilDate.getTime())
        ));

        return "Upload SuccessFully!";
    }
}
