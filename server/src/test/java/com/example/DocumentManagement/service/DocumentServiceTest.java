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
import com.example.DocumentManagement.response.ListResponse;
import com.example.DocumentManagement.response.MessageResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Date;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
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

    @Test
    void givenNewDocumentDataWithTrueDepartmentId_whenCreateDocuments_thenReturnMessageSuccessfully() {
        // Given
        CreateDocumentRequest newDocumentData = new CreateDocumentRequest(
                "doc 1",
                "description for doc 1",
                "http://example.com/document/1",
                "Version 1.0.0",
                "note for doc 1",
                "1");
        MessageResponse expectedResponse = new MessageResponse("Create SuccessFully!");
        DepartmentEntity departmentEntity = new DepartmentEntity("Management", "Management Room");
        DocumentEntity documentEntity = new DocumentEntity(
                "doc 1",
                "description for doc 1",
                Date.valueOf("2023-11-10"),
                false,
                null,
                "1");
        documentEntity.setId(1);
        VersionEntity versionEntity = new VersionEntity(1, "https://example.com/version/1", "Version 1.0.0", true, Date.valueOf("2023-11-10"), "Initial version");
        // Mock repository calls
        when(departmentRepository.findDepartmentById(1)).thenReturn(departmentEntity);
        when(documentRepository.save(any(DocumentEntity.class))).thenReturn(documentEntity);
        when(versionRepository.save(any(VersionEntity.class))).thenReturn(versionEntity);
        // When
        MessageResponse results = documentService.createDocument(newDocumentData);
        // Then
        Assertions.assertEquals(expectedResponse, results);
        // Verify repository calls
        verify(departmentRepository).findDepartmentById(Integer.parseInt(newDocumentData.getDepartmentId()));
    }

    @Test
    void givenNewDocumentDataWithWrongDepartmentId_whenCreateDocuments_thenException() {
        // Given
        CreateDocumentRequest newDocumentData = new CreateDocumentRequest(
                "doc 1",
                "description for doc 1",
                "http://example.com/document/1",
                "Version 1.0.0",
                "note for doc 1",
                "1");
        MessageResponse expectedResponse = new MessageResponse("Department not found!");
        // Mock repository calls
        when(departmentRepository.findDepartmentById(1)).thenReturn(null);
        // When
        Exception exception = assertThrows(NotFoundException.class, () -> {
            documentService.createDocument(newDocumentData);
        });
        // Then
        Assertions.assertEquals(expectedResponse.getMessage(), exception.getMessage());
        // Verify repository calls
        verify(departmentRepository).findDepartmentById(Integer.parseInt(newDocumentData.getDepartmentId()));
    }

    @Test
    void givenZeroDocumentsWithWrongDepartmentId_whenGetDocumentsByDepartmentId_thenException() {
        // Given
        String departmentId = "1";
        MessageResponse expectedResponse = new MessageResponse("Document not found!");
        // Mock repository calls
        when(documentRepository.findDocumentsByDepartmentId(Integer.parseInt(departmentId))).thenReturn(null);
        // When
        Exception exception = assertThrows(NotFoundException.class, () -> {
            documentService.getDocumentsByDepartmentId(departmentId);
        });
        // Then
        Assertions.assertEquals(expectedResponse.getMessage(), exception.getMessage());
        // Verify repository calls
        verify(documentRepository).findDocumentsByDepartmentId(Integer.parseInt(departmentId));
    }

    @Test
    void givenDocumentsWithTrueDepartmentId_whenGetDocumentsByDepartmentId_thenReturnDocuments() {
        // Given
        String departmentId = "1";
        DepartmentEntity departmentEntity = new DepartmentEntity("Management", "Management Room");
        DocumentEntity document1 = new DocumentEntity("Document1", "Document1 Description", Date.valueOf("2023-11-10"), false, null, "1");
        document1.setId(1);
        VersionEntity version1doc1 = new VersionEntity(1, "https://example.com/version/1", "Version 1.0.0", true, Date.valueOf("2023-11-10"), "Original");
        VersionEntity version2doc1 = new VersionEntity(1, "https://example.com/version/1", "Version 1.0.1", true, Date.valueOf("2023-11-10"), "Update");
        DocumentEntity document2 = new DocumentEntity("Document2", "Document2 Description", Date.valueOf("2023-11-10"), false, null, "1");
        document2.setId(2);
        VersionEntity version1doc2 = new VersionEntity(2, "https://example.com/version/2", "Version 2.0.0", true, Date.valueOf("2023-11-10"), "Original");
        VersionEntity version2doc2 = new VersionEntity(2, "https://example.com/version/2", "Version 2.0.1", true, Date.valueOf("2023-11-10"), "Update");
        List<DocumentEntity> documents = Arrays.asList(document1, document2);
        List<VersionEntity> listVersion1 = Arrays.asList(version1doc1, version1doc2);
        List<VersionEntity> listVersion2 = Arrays.asList(version2doc1, version2doc2);

        DocumentResponse documentResponse1 = new DocumentResponse(document1.getId(), document1.getName(), document1.getDescription(), document1.getCreateTime(), departmentEntity, listVersion1);
        DocumentResponse documentResponse2 = new DocumentResponse(document2.getId(), document2.getName(), document2.getDescription(), document2.getCreateTime(), departmentEntity, listVersion2);
        ListResponse expectedResult = new ListResponse(Arrays.asList(documentResponse1, documentResponse2));
        // Mock repository calls
        when(documentRepository.findDocumentsByDepartmentId(Integer.parseInt(departmentId))).thenReturn(documents);
        when(versionRepository.findByDocumentIdAndCurrentVersionTrue(1)).thenReturn(listVersion1);
        when(versionRepository.findByDocumentIdAndCurrentVersionTrue(2)).thenReturn(listVersion2);
        when(departmentRepository.findDepartmentById(1)).thenReturn(departmentEntity);

        // When
        ListResponse results = documentService.getDocumentsByDepartmentId(departmentId);
        // Then
        Assertions.assertEquals(expectedResult.getListContent(), results.getListContent());
        // Verify repository calls
        verify(documentRepository).findDocumentsByDepartmentId(Integer.parseInt(departmentId));
        verify(departmentRepository, times(2)).findDepartmentById(1);
        verify(versionRepository).findByDocumentIdAndCurrentVersionTrue(1);
        verify(versionRepository).findByDocumentIdAndCurrentVersionTrue(2);
    }

    @Test
    void givenZeroDocumentsWithTrueDepartmentId_whenGetDocumentsByDepartmentId_thenReturnZeroDocuments() {
        // Given
        String departmentId = "1";
        List<DocumentEntity> documents = Arrays.asList();
        ListResponse expectedResult = new ListResponse(Arrays.asList());
        // Mock repository calls
        when(documentRepository.findDocumentsByDepartmentId(Integer.parseInt(departmentId))).thenReturn(documents);
        // When
        ListResponse results = documentService.getDocumentsByDepartmentId(departmentId);
        // Then
        Assertions.assertEquals(expectedResult.getListContent(), results.getListContent());
        // Verify repository calls
        verify(documentRepository).findDocumentsByDepartmentId(Integer.parseInt(departmentId));
    }

    @Test
    void givenWrongDocumentId_whenDeleteDocumentById_thenException() {
        // Given
        String documentId = "1";
        MessageResponse expectedResponse = new MessageResponse("Document not found!");
        // Mock repository calls
        when(documentRepository.findDocumentById(Integer.parseInt(documentId))).thenReturn(null);
        // When
        Exception exception = assertThrows(NotFoundException.class, () -> {
            documentService.deleteDocumentById(documentId);
        });
        // Then
        Assertions.assertEquals(expectedResponse.getMessage(), exception.getMessage());
        // Verify repository calls
        verify(documentRepository).findDocumentById(Integer.parseInt(documentId));
    }

    @Test
    void givenTrueDocumentId_whenDeleteDocumentById_thenReturnMessageSuccessfully() {
        // Given
        String documentId = "1";
        DocumentEntity document = new DocumentEntity("Document1", "Document1 Description", Date.valueOf("2023-11-10"), false, null, "1");
        document.setId(1);
        MessageResponse expectedResponse = new MessageResponse("Delete Document SuccessFully!");
        // Mock repository calls
        when(documentRepository.findDocumentById(Integer.parseInt(documentId))).thenReturn(document);
        // When
        MessageResponse response = documentService.deleteDocumentById(documentId);
        // Then
        Assertions.assertEquals(expectedResponse.getMessage(), response.getMessage());
        // Verify repository calls
        verify(documentRepository).findDocumentById(Integer.parseInt(documentId));
    }

    @Test
    void givenKeywordAndDepartmentId_whenSearchDocuments_thenReturnMatchingDocuments() {
        // Given
        int departmentId = 1;
        String keyword = "example";
        DocumentEntity document1 = new DocumentEntity("Document1", "Description1", null, false, null, "1");
        DocumentEntity document2 = new DocumentEntity("Document2", "Description2", null, false, null, "1");
        List<DocumentEntity> expectedDocuments = Arrays.asList(document1, document2);
        // Mock the behavior of the repository method
        when(documentRepository.searchDocuments(departmentId,keyword)).thenReturn(expectedDocuments);
        // When
        List<DocumentEntity> actualDocuments = documentRepository.searchDocuments(departmentId, keyword);
        // Then
        Assertions.assertEquals(expectedDocuments.size(), actualDocuments.size());
        Assertions.assertEquals(expectedDocuments, actualDocuments);
        verify(documentRepository).searchDocuments(departmentId,keyword);
    }
    @Test
    void givenNoMatchingKeyword_whenSearchDocuments_thenReturnEmptyList() {
        // Given
        int departmentId = 1;
        String keyword = "nonexistent";
        List<DocumentEntity> emptyList = Collections.emptyList();

        // Mock the repository behavior when no documents match the given keyword
        when(documentRepository.searchDocuments(departmentId, keyword)).thenReturn(emptyList);

        // When
        List<DocumentEntity> actualDocuments = documentRepository.searchDocuments(departmentId, keyword);

        // Then
        Assertions.assertEquals(0, actualDocuments.size());
        Assertions.assertEquals(emptyList, actualDocuments);
        verify(documentRepository).searchDocuments(departmentId, keyword);
    }
    @Test
    void givenValidDocumentId_whenUpdateDocument_thenUpdateSuccessfully() {
        // Given
        int documentId = 1;
        UpdateDocumentRequest updateRequest = new UpdateDocumentRequest();
        updateRequest.setNameDocument("Updated Document");
        updateRequest.setDescription("Updated Description");
        updateRequest.setUrl("https://updated-url.com");
        updateRequest.setNameVersion("Version 2.0");
        updateRequest.setNote("Updated Note");

        LocalDateTime currentDateTime = LocalDateTime.now();
        Date createTime = new Date(Date.from(currentDateTime.atZone(ZoneId.systemDefault()).toInstant()).getTime());

        DocumentEntity existingDocument = new DocumentEntity("Existing Document", "Existing Description", createTime, false, null, "1");

        when(documentRepository.findDocumentById(documentId)).thenReturn(existingDocument);
        when(versionRepository.findByDocumentIdAndCurrentVersionTrue(documentId)).thenReturn(Collections.emptyList());

        // When
        MessageResponse response = documentService.updateDocument(updateRequest, String.valueOf(documentId));

        // Then
        Assertions.assertEquals("Update Document SuccessFully!", response.getMessage());


    }
    @Test
    void givenInvalidDocumentId_whenUpdateDocument_thenThrowNotFoundException() {
        // Given
        int documentId = 2;
        UpdateDocumentRequest updateRequest = new UpdateDocumentRequest();

        when(documentRepository.findDocumentById(documentId)).thenReturn(null);

        // When & Then
        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> documentService.updateDocument(updateRequest, String.valueOf(documentId)));

        Assertions.assertEquals("Document not found!", exception.getMessage());

        verify(documentRepository).findDocumentById(documentId);
        verifyNoInteractions(versionRepository);
    }

}
