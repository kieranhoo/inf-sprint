package com.example.DocumentManagement.service;

import com.example.DocumentManagement.entity.DocumentEntity;
import com.example.DocumentManagement.entity.VersionEntity;
import com.example.DocumentManagement.exception.NotFoundException;
import com.example.DocumentManagement.repository.DocumentRepository;
import com.example.DocumentManagement.repository.VersionRepository;
import com.example.DocumentManagement.request.UpdateDocumentRequest;
import com.example.DocumentManagement.response.GetAllDocumentResponse;
import com.example.DocumentManagement.response.MessageResponse;
import com.example.DocumentManagement.response.PageResponse;
import com.example.DocumentManagement.supportFunction.SupportFunction;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DocumentService extends SupportFunction {
    private final DocumentRepository documentRepository;
    private final VersionRepository versionRepository;

    public MessageResponse createDocument(UpdateDocumentRequest updateDocumentRequest) {
        LocalDateTime currentDateTime = LocalDateTime.now();
        java.util.Date utilDate = Date.from(currentDateTime.atZone(ZoneId.systemDefault()).toInstant());

        updateDocumentRequest.setNameVersion("1.0.0");

        DocumentEntity documentEntity = documentRepository.save(new DocumentEntity(
                updateDocumentRequest.getNameDocument(),
                updateDocumentRequest.getDescription(),
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

        return new MessageResponse("Create SuccessFully!");
    }

    public MessageResponse updateDocument(UpdateDocumentRequest updateDocumentRequest, String idFromPathVariable) {
        int id = checkRequest(idFromPathVariable);

        DocumentEntity documentEntity = documentRepository.findDocumentById(id);
        if(documentEntity == null) {
            throw new NotFoundException("Don't exit Document.");
        }

        LocalDateTime currentDateTime = LocalDateTime.now();
        Date createTime = new Date(Date.from(currentDateTime.atZone(ZoneId.systemDefault()).toInstant()).getTime());

        documentRepository.updateDocument(
                updateDocumentRequest.getNameDocument(),
                updateDocumentRequest.getDescription(),
                createTime,
                id
        );

        //set field "current version" of another version is false
        List<VersionEntity> listVersion = versionRepository.findByDocumentIdAndCurrentVersionTrue(id);
        for(VersionEntity loop : listVersion) {
            versionRepository.updateCurrentVersion(loop.getId());
        }

        versionRepository.save(new VersionEntity(
                id,
                updateDocumentRequest.getUrl(),
                updateDocumentRequest.getNameVersion(),
                true,
                createTime
        ));

        return new MessageResponse("Update Document SuccessFully!");
    }

    public PageResponse getAllDocuments(String pageFromParam, String sizeFromParam) {
        int page = checkPage(pageFromParam);
        int size = checkSize(sizeFromParam);

        if (size >= 100) size = 100;
        if (page < 0) page = 1;
        if (size < 0) size = 10;

        Pageable pageable = PageRequest.of(page - 1, size);
        Page<DocumentEntity> pageDocument = documentRepository.findAllPageByIsDeletedFalse(pageable);

        List<GetAllDocumentResponse> response = new ArrayList<>();
        for(DocumentEntity loop : pageDocument.getContent()) {
            response.add(new GetAllDocumentResponse(
                    loop.getId(),
                    loop.getName(),
                    loop.getDescription(),
                    loop.getCreateTime()
            ));
        }

        return new PageResponse(pageDocument.getTotalElements(), pageDocument.getTotalPages(), response);
    }
    public MessageResponse deleteDocumentById(String idFromPathVariable){
        int id = checkRequest(idFromPathVariable);

        LocalDateTime currentDateTime = LocalDateTime.now();
        Date deleteTime = new Date(Date.from(currentDateTime.atZone(ZoneId.systemDefault()).toInstant()).getTime());

        documentRepository.deleteDocumentById(Boolean.TRUE,deleteTime,id);

        return new MessageResponse("Delete Document SuccessFully!");
    }
}
