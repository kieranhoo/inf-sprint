package com.example.DocumentManagement.service;

import com.example.DocumentManagement.entity.DepartmentEntity;
import com.example.DocumentManagement.entity.DocumentEntity;
import com.example.DocumentManagement.entity.VersionEntity;
import com.example.DocumentManagement.exception.NotFoundException;
import com.example.DocumentManagement.repository.DepartmentRepository;
import com.example.DocumentManagement.repository.DocumentRepository;
import com.example.DocumentManagement.repository.VersionRepository;
import com.example.DocumentManagement.response.DocumentResponse;
import com.example.DocumentManagement.response.ListResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Date;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DocumentServiceTest {
    @InjectMocks
    private DocumentService documentService;
    @Mock
    private DocumentRepository documentRepository;
    @Mock
    private VersionRepository versionRepository;
    @Mock
    private DepartmentRepository departmentRepository;
    @Test
    void givenDocuments_whenGetAllDocuments_thenReturnAllDocuments() {
        // Given
        DepartmentEntity departmentEntity = new DepartmentEntity("Management", "Management Room");
        DocumentEntity document1 = new DocumentEntity("Document1", "Document1 Description", Date.valueOf("2023-11-10"), false, null, "1");
        document1.setId(1);
        VersionEntity version1doc1 = new VersionEntity(1, "https://example.com/version/1", "Version 1.0.0", false, Date.valueOf("2023-11-10"), "Original");
        VersionEntity version2doc1 = new VersionEntity(1, "https://example.com/version/1", "Version 1.0.1", true, Date.valueOf("2023-11-10"), "Update");
        DocumentEntity document2 = new DocumentEntity("Document2", "Document2 Description", Date.valueOf("2023-11-10"), false, null, "1");
        document2.setId(2);
        VersionEntity version1doc2 = new VersionEntity(2, "https://example.com/version/2", "Version 2.0.0", false, Date.valueOf("2023-11-10"), "Original");
        VersionEntity version2doc2 = new VersionEntity(2, "https://example.com/version/2", "Version 2.0.1", true, Date.valueOf("2023-11-10"), "Update");
        List<DocumentEntity> documents = Arrays.asList(document1, document2);
        List<VersionEntity> listVersion1 = Arrays.asList(version1doc1, version1doc2);
        List<VersionEntity> listVersion2 = Arrays.asList(version2doc1, version2doc2);

        DocumentResponse documentResponse1 = new DocumentResponse(document1.getId(), document1.getName(), document1.getDescription(), document1.getCreateTime(), departmentEntity, listVersion1);
        DocumentResponse documentResponse2 = new DocumentResponse(document2.getId(), document2.getName(), document2.getDescription(), document2.getCreateTime(), departmentEntity, listVersion2);
        ListResponse expectedResult = new ListResponse(Arrays.asList(documentResponse1, documentResponse2));
        // Mock repository calls
        when(documentRepository.findAllDocuments()).thenReturn(documents);
        when(versionRepository.findByDocumentIdAndCurrentVersionTrue(1)).thenReturn(listVersion1);
        when(versionRepository.findByDocumentIdAndCurrentVersionTrue(2)).thenReturn(listVersion2);
        when(departmentRepository.findDepartmentById(1)).thenReturn(departmentEntity);

        // When
        ListResponse results = documentService.getAllDocuments();
        // Then
        Assertions.assertEquals(expectedResult.getListContent(), results.getListContent());
        // Verify repository calls
        verify(documentRepository).findAllDocuments();
        verify(departmentRepository, times(2)).findDepartmentById(1);
        verify(versionRepository).findByDocumentIdAndCurrentVersionTrue(1);
        verify(versionRepository).findByDocumentIdAndCurrentVersionTrue(2);
    }
    @Test
    void givenZeroDocuments_whenGetAllDocuments_thenReturnZeroDocuments() {
        // Given

        // Mock repository calls
        when(documentRepository.findAllDocuments()).thenReturn(Collections.emptyList());

        // When
        ListResponse results = documentService.getAllDocuments();

        // Then
        Assertions.assertEquals(Collections.emptyList(), results.getListContent());
        // Verify repository calls
        verify(documentRepository).findAllDocuments();
    }

    @Test
    void givenTrueId_whenFindDocumentById_thenReturnDocument() {
        // Given
        DocumentEntity expectedDocument = new DocumentEntity("Document1", "Document1 Description", Date.valueOf("2023-11-10"), false, null, "1");
        expectedDocument.setId(1);
        VersionEntity version1 = new VersionEntity(expectedDocument.getId(), "https://example.com/version/1", "Version 1.0.0", false, Date.valueOf("2023-11-10"), "Initial version");
        VersionEntity version2 = new VersionEntity(expectedDocument.getId(), "https://example.com/version/2", "Version 1.0.1", true, Date.valueOf("2023-11-11"), "Update version");
        List<VersionEntity> listVersions = Arrays.asList(version1, version2);
        DepartmentEntity departmentEntity = new DepartmentEntity("Management", "Management Room");
        when(documentRepository.findDocumentById(expectedDocument.getId())).thenReturn(expectedDocument);
        when(departmentRepository.findDepartmentById(Integer.parseInt(expectedDocument.getDepartmentId()))).thenReturn(departmentEntity);
        when(versionRepository.findByDocumentId(expectedDocument.getId())).thenReturn(listVersions);

        DocumentResponse expectedDocumentResponse = new DocumentResponse(
                expectedDocument.getId(),
                expectedDocument.getName(),
                expectedDocument.getDescription(),
                expectedDocument.getCreateTime(),
                departmentEntity,
                listVersions
        );
        // When
        DocumentResponse result = documentService.getDocumentById((expectedDocument.getId()).toString());
        // Then
        Assertions.assertEquals(expectedDocumentResponse, result);
        // Verify repository calls
        verify(documentRepository).findDocumentById(1);
        verify(departmentRepository).findDepartmentById(1);
        verify(versionRepository).findByDocumentId(1);
    }

    @Test
    void givenWrongId_whenFindDocumentById_thenReturnException() {
        // Given
        when(documentRepository.findDocumentById(2)).thenReturn(null);

        // When
        NotFoundException exception = Assertions.assertThrows(NotFoundException.class, () -> {
            documentService.getDocumentById("2");
        });
        // Then
        Assertions.assertEquals("Document not found!", exception.getMessage());
        // Verify repository calls
        verify(documentRepository).findDocumentById(2);
    }
}
