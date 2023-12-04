package com.example.DocumentManagement.controller;

import com.example.DocumentManagement.entity.DepartmentEntity;
import com.example.DocumentManagement.entity.VersionEntity;
import com.example.DocumentManagement.exception.NotFoundException;
import com.example.DocumentManagement.response.*;
import com.example.DocumentManagement.service.DocumentService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.swing.text.Document;
import java.sql.Date;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;
@ExtendWith(MockitoExtension.class)
public class DocumentControllerTest {
    @InjectMocks
    DocumentController documentController;
    @Mock
    DocumentService documentService;
    @Test
    void givenTrueId_whenGetDocumentById_thenReturnDocumentResponse () {
        // Given
        String idString = "123";
        int id = 123;
        DepartmentEntity department = new DepartmentEntity();
        department.setName("IT Department");
        department.setDescription("Handles IT-related tasks");

        VersionEntity version1 = new VersionEntity();
        version1.setDocumentId(id);
        version1.setUrl("http://example.com/document/1");
        version1.setName("Version 1");
        version1.setCurrentVersion(false);
        version1.setUpdateTime(Date.valueOf("2023-11-09"));
        version1.setNote("Initial version");

        VersionEntity version2 = new VersionEntity();
        version2.setDocumentId(id);
        version2.setUrl("http://example.com/document/1");
        version2.setName("Version 2");
        version2.setCurrentVersion(true);
        version2.setUpdateTime(Date.valueOf("2023-11-10"));
        version2.setNote("Updated version");

        List<VersionEntity> versions = Arrays.asList(version1, version2);

        DocumentResponse documentResponse = new DocumentResponse();
        documentResponse.setId(id);
        documentResponse.setName("Mocked Document");
        documentResponse.setDescription("Mocked Description");
        documentResponse.setCreateTime(Date.valueOf("2023-11-10"));
        documentResponse.setDepartment(department);
        documentResponse.setVersions(versions);

        when(documentService.getDocumentById(idString)).thenReturn(documentResponse);
        // When
        ResponseEntity<DocumentResponse> result = documentController.getDocumentById(idString);
        // Then
        Assertions.assertEquals(HttpStatus.OK, result.getStatusCode());
        Assertions.assertEquals(documentResponse, result.getBody());
        verify(documentService).getDocumentById(idString);
    }
    @Test
    void givenWrongId_whenGetDocumentById_thenReturnException () {
        // Given
        when(documentService.getDocumentById("2")).thenThrow(new NotFoundException("Document not found!"));
        // When
        NotFoundException exception = Assertions.assertThrows(NotFoundException.class, () -> {
            documentService.getDocumentById("2");
        });
        // Then
        Assertions.assertEquals("Document not found!", exception.getMessage());
        verify(documentService).getDocumentById("2");
    }

    @Test
    void givenDocument_whenGetAllDocuments_thenReturnListDocumentsResponse () {
        // Given
        int id = 123;
        DepartmentEntity department = new DepartmentEntity();
        department.setName("IT Department");
        department.setDescription("Handles IT-related tasks");

        VersionEntity version1 = new VersionEntity();
        version1.setDocumentId(id);
        version1.setUrl("http://example.com/document/1");
        version1.setName("Version 1");
        version1.setCurrentVersion(false);
        version1.setUpdateTime(Date.valueOf("2023-11-09"));
        version1.setNote("Initial version");

        VersionEntity version2 = new VersionEntity();
        version2.setDocumentId(id);
        version2.setUrl("http://example.com/document/1");
        version2.setName("Version 2");
        version2.setCurrentVersion(true);
        version2.setUpdateTime(Date.valueOf("2023-11-10"));
        version2.setNote("Updated version");

        List<VersionEntity> versions = Arrays.asList(version1, version2);

        DocumentResponse documentResponse = new DocumentResponse();
        documentResponse.setId(id);
        documentResponse.setName("Mocked Document");
        documentResponse.setDescription("Mocked Description");
        documentResponse.setCreateTime(Date.valueOf("2023-11-10"));
        documentResponse.setDepartment(department);
        documentResponse.setVersions(versions);

        ListResponse expectedResponse = new ListResponse(Arrays.asList(documentResponse));
        when(documentService.getAllDocuments()).thenReturn(expectedResponse);
        // When
        ResponseEntity<ListResponse> result = documentController.getAllDocuments();
        // Then
        Assertions.assertEquals(HttpStatus.OK, result.getStatusCode());
        Assertions.assertEquals(expectedResponse, result.getBody());
        verify(documentService).getAllDocuments();
    }
    @Test
    void givenZeroDocuments_whenGetAllDocuments_thenReturnListZeroDocumentsResponse () {
        // Given
        when(documentService.getAllDocuments()).thenReturn(new ListResponse(Collections.emptyList()));
        // When
        ResponseEntity<ListResponse> result = documentController.getAllDocuments();
        // Then
        Assertions.assertEquals(HttpStatus.OK, result.getStatusCode());
        Assertions.assertEquals(0, result.getBody().getListContent().size());
        verify(documentService).getAllDocuments();
    }
}
