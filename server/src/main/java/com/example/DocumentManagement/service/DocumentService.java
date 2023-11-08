package com.example.DocumentManagement.service;

import com.example.DocumentManagement.entity.DepartmentEntity;
import com.example.DocumentManagement.entity.DocumentEntity;
import com.example.DocumentManagement.entity.VersionEntity;
import com.example.DocumentManagement.exception.NotFoundException;
import com.example.DocumentManagement.repository.DepartmentRepository;
import com.example.DocumentManagement.repository.DocumentRepository;
import com.example.DocumentManagement.repository.VersionRepository;
import com.example.DocumentManagement.request.CreateDocumentRequest;
import com.example.DocumentManagement.request.UpdateDocumentRequest;
import com.example.DocumentManagement.response.DocumentResponse;
import com.example.DocumentManagement.response.GetAllDocumentResponse;
import com.example.DocumentManagement.response.ListResponse;
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
    private final DepartmentRepository DepartmentRepository;

    public MessageResponse createDocument(CreateDocumentRequest createDocumentRequest) {
        createDocumentRequest.setNameVersion("Version 1.0.0");
        LocalDateTime currentDateTime = LocalDateTime.now();
        java.util.Date utilDate = Date.from(currentDateTime.atZone(ZoneId.systemDefault()).toInstant());

        DepartmentEntity departmentEntity = DepartmentRepository.findDepartmentById(Integer.parseInt(createDocumentRequest.getDepartmentId()));

        if (departmentEntity == null) {
            throw new NotFoundException("Department not found!");
        }

        DocumentEntity documentEntity = documentRepository.save(new DocumentEntity(
                createDocumentRequest.getNameDocument(),
                createDocumentRequest.getDescription(),
                new Date(utilDate.getTime()),
                false,
                null,
                createDocumentRequest.getDepartmentId()
        ));

        versionRepository.save(new VersionEntity(
                documentEntity.getId(),
                createDocumentRequest.getUrl(),
                createDocumentRequest.getNameVersion(),
                true,
                new Date(utilDate.getTime()),
                createDocumentRequest.getNote()
        ));

        return new MessageResponse("Create SuccessFully!");
    }

    public DocumentResponse getDocumentById(String idFromPathVariable) {
        int id = checkRequest(idFromPathVariable);

        DocumentEntity documentEntity = documentRepository.findDocumentById(id);
        if (documentEntity == null) {
            throw new NotFoundException("Document not found!");
        }

        List<VersionEntity> listVersion = versionRepository.findByDocumentId(id);

        return new DocumentResponse(
                documentEntity.getId(),
                documentEntity.getName(),
                documentEntity.getDescription(),
                documentEntity.getCreateTime(),
                DepartmentRepository.findDepartmentById(Integer.parseInt(documentEntity.getDepartmentId())),
                listVersion
        );
    }

    public MessageResponse updateDocument(UpdateDocumentRequest updateDocumentRequest, String idFromPathVariable) {
        int id = checkRequest(idFromPathVariable);

        DocumentEntity documentEntity = documentRepository.findDocumentById(id);
        if (documentEntity == null) {
            throw new NotFoundException("Document not found!");
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
        for (VersionEntity loop : listVersion) {
            versionRepository.updateCurrentVersion(loop.getId());
        }

        versionRepository.save(new VersionEntity(
                id,
                updateDocumentRequest.getUrl(),
                updateDocumentRequest.getNameVersion(),
                true,
                createTime,
                updateDocumentRequest.getNote()
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
        for (DocumentEntity loop : pageDocument.getContent()) {
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

    public ListResponse getDocumentsByDepartmentId(String idFromPathVariable) {
        int id = checkRequest(idFromPathVariable);
        List<DocumentEntity> documents = documentRepository.findDocumentsByDepartmentId(id);
        List<DocumentResponse> list = new ArrayList<>();
        for (DocumentEntity loop : documents) {
            List<VersionEntity> listVersion = versionRepository.findByDocumentIdAndCurrentVersionTrue(loop.getId());
            list.add(new DocumentResponse(
                    loop.getId(),
                    loop.getName(),
                    loop.getDescription(),
                    loop.getCreateTime(),
                    DepartmentRepository.findDepartmentById(Integer.parseInt(loop.getDepartmentId())),
                    listVersion
            ));
        }
        return new ListResponse(list);
    }

    public ListResponse searchDocuments(Integer departmentID, String keyword) {
        List<DocumentEntity> documents = documentRepository.searchDocuments(departmentID, keyword);
        return new ListResponse(documents);
    }
}
