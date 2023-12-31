package com.example.DocumentManagement.controller;

import com.example.DocumentManagement.entity.DepartmentEntity;
import com.example.DocumentManagement.entity.DocumentEntity;
import com.example.DocumentManagement.entity.VersionEntity;
import com.example.DocumentManagement.exception.BadRequestException;
import com.example.DocumentManagement.request.CreateDocumentRequest;
import com.example.DocumentManagement.exception.NotFoundException;
import com.example.DocumentManagement.request.SearchDocumentRequest;
import com.example.DocumentManagement.request.UpdateDocumentRequest;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
        NotFoundException exception = assertThrows(NotFoundException.class, () -> {
            documentService.getDocumentById("2");
        });
        // Then
        Assertions.assertEquals("Document not found!", exception.getMessage());
        verify(documentService).getDocumentById("2");
    }
    @Test
    void givenNullId_whenGetDocumentById_thenReturnException() {
        // Given
        when(documentService.getDocumentById(null)).thenThrow(new BadRequestException("Request must be a Integer"));

        // When
        BadRequestException exception = assertThrows(BadRequestException.class, () -> {
            documentService.getDocumentById(null);
        });

        // Then
        Assertions.assertEquals("Request must be a Integer", exception.getMessage());
        verify(documentService).getDocumentById(null);
    }
    @Test
    void givenInvalidId_whenGetDocumentById_thenReturnException() {
        // Given
        when(documentService.getDocumentById("abcd")).thenThrow(new BadRequestException("Request must be a Integer"));

        // When
        BadRequestException exception = assertThrows(BadRequestException.class, () -> {
            documentService.getDocumentById("abcd");
        });

        // Then
        Assertions.assertEquals("Request must be a Integer", exception.getMessage());
        verify(documentService).getDocumentById("abcd");
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

    @Test
    void givenNewDocumentDataWithTrueDepartmentId_whenCreateDocuments_thenReturnOK() {
        // Given
        CreateDocumentRequest newDocumentData = new CreateDocumentRequest("doc 1", "description for doc 1", "http://example.com/document/1", "Version 1.0.0", "note for doc 1", "1");
        MessageResponse expectedResponse = new MessageResponse("Create SuccessFully!");
        when(documentService.createDocument(newDocumentData)).thenReturn(expectedResponse);

        // When
        ResponseEntity<MessageResponse> responseEntity = documentController.createDocument(newDocumentData);

        // Then
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedResponse, responseEntity.getBody());
        verify(documentService).createDocument(newDocumentData);
    }

    @Test
    void givenNewDocumentDataWithWrongDepartmentId_whenCreateDocuments_thenException() {
        // Given
        CreateDocumentRequest newDocumentData = new CreateDocumentRequest("doc 1", "description for doc 1", "http://example.com/document/1", "Version 1.0.0", "note for doc 1", "1");
        NotFoundException expectedException = new NotFoundException("Department not found!");
        when(documentService.createDocument(newDocumentData)).thenThrow(expectedException);

        // When
        Exception exception = assertThrows(NotFoundException.class, () ->{
            documentController.createDocument(newDocumentData);
        });

        // Then
        Assertions.assertEquals(expectedException, exception);
        verify(documentService).createDocument(newDocumentData);
    }

    @Test
    void givenDocumentsWithWrongDepartmentId_whenFindAllDocumentsByDepartmentId_thenException() {
        // Given
        String departmentId = "-1";
        NotFoundException expectedException = new NotFoundException("Department not found!");
        when(documentService.getDocumentsByDepartmentId(departmentId)).thenThrow(expectedException);

        // When
        Exception exception = assertThrows(NotFoundException.class, () ->{
            documentService.getDocumentsByDepartmentId(departmentId);
        });

        // Then
        Assertions.assertEquals(expectedException, exception);
        verify(documentService).getDocumentsByDepartmentId(departmentId);
    }

    @Test
    void givenDocumentsWithTrueDepartmentId_whenFindAllDocumentsByDepartmentId_thenReturnOK() {
        // Given
        String departmentId = "1";
        DocumentEntity document1 = new DocumentEntity("doc 1", "description for doc 1", Date.valueOf("2023-11-10"), false, null, "1");
        DocumentEntity document2 = new DocumentEntity("doc 2", "description for doc 2", Date.valueOf("2023-11-11"), false, null, "1");
        ListResponse expectedResponse = new ListResponse(Arrays.asList(document1, document2));
        when(documentService.getDocumentsByDepartmentId(departmentId)).thenReturn(expectedResponse);

        // When
        ResponseEntity<ListResponse> responseEntity = documentController.getDocumentsByDepartmentId(departmentId);

        // Then
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedResponse, responseEntity.getBody());
        verify(documentService).getDocumentsByDepartmentId(departmentId);
    }

    @Test
    void givenZeroDocumentsWithTrueDepartmentId_whenFindAllDocumentsByDepartmentId_thenReturnOK() {
        // Given
        String departmentId = "1";
        ListResponse expectedResponse = new ListResponse(List.of());
        when(documentService.getDocumentsByDepartmentId(departmentId)).thenReturn(expectedResponse);

        // When
        ResponseEntity<ListResponse> responseEntity = documentController.getDocumentsByDepartmentId(departmentId);

        // Then
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedResponse, responseEntity.getBody());
        verify(documentService).getDocumentsByDepartmentId(departmentId);
    }

    @Test
    void givenTrueDocumentId_whenDeleteDocumentById_thenReturnOK() {
        // Given
        String documentId = "1";
        MessageResponse expectedResponse = new MessageResponse("Create SuccessFully!");
        when(documentService.deleteDocumentById(documentId)).thenReturn(expectedResponse);

        // When
        ResponseEntity<?> responseEntity = documentController.deleteDocumentById(documentId);

        // Then
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedResponse, responseEntity.getBody());
        verify(documentService).deleteDocumentById(documentId);
    }

    @Test
    void givenWrongDocumentId_whenDeleteDocumentById_thenException() {
        // Given
        String documentId = "1";
        NotFoundException expectedException = new NotFoundException("Document not found!");
        when(documentService.deleteDocumentById(documentId)).thenThrow(expectedException);

        // When
        Exception exception = assertThrows(NotFoundException.class, () ->{
            documentController.deleteDocumentById(documentId);
        });

        // Then
        Assertions.assertEquals(expectedException, exception);
        verify(documentService).deleteDocumentById(documentId);
    }

    @Test
    void givenValidDocumentId_whenUpdateDocument_thenReturnOK() {
        // Given
        String id = "123";
        UpdateDocumentRequest updateDocumentRequest = new UpdateDocumentRequest("Updated Document", "Updated Description", "http://example.com/document/1", "Version 2.0.0", "Note for update");

        MessageResponse expectedResponse = new MessageResponse("Update Document SuccessFully!");

        when(documentService.updateDocument(updateDocumentRequest, id)).thenReturn(expectedResponse);

        // When
        ResponseEntity<MessageResponse> responseEntity = documentController.updateDocument(id, updateDocumentRequest);

        // Then
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedResponse, responseEntity.getBody());
        verify(documentService).updateDocument(updateDocumentRequest, id);
    }
    @Test
    void givenInvalidDocumentId_whenUpdateDocument_thenReturnNotFound() {
        // Given
        String id = "999"; // Assuming this ID does not exist
        UpdateDocumentRequest updateDocumentRequest = new UpdateDocumentRequest("Updated Document", "Updated Description", "http://example.com/document/1", "Version 2.0.0", "Note for update");

        when(documentService.updateDocument(updateDocumentRequest, id)).thenThrow(new NotFoundException("Document not found!"));

        // When
        NotFoundException exception = assertThrows(NotFoundException.class, () -> {
            documentController.updateDocument(id, updateDocumentRequest);
        });

        // Then
        assertEquals("Document not found!", exception.getMessage());
        verify(documentService).updateDocument(updateDocumentRequest, id);
    }
    @Test
    void givenSearchDocumentRequest_whenSearchDocuments_thenReturnMatchingDocuments() {
        // Given
        SearchDocumentRequest searchRequest = new SearchDocumentRequest(1, "keyword");

        DocumentEntity document1 = new DocumentEntity("Document1", "Description1", null, false, null, "1");
        DocumentEntity document2 = new DocumentEntity("Document2", "Description2", null, false, null, "1");
        List<DocumentEntity> expectedDocuments = Arrays.asList(document1, document2);

        ListResponse expectedResponse = new ListResponse(expectedDocuments);

        when(documentService.searchDocuments(searchRequest.getDepartmentID(), searchRequest.getKeyword())).thenReturn(expectedResponse);

        // When
        ResponseEntity<ListResponse> responseEntity = documentController.searchDocuments(searchRequest);

        // Then
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedResponse, responseEntity.getBody());
        verify(documentService).searchDocuments(searchRequest.getDepartmentID(), searchRequest.getKeyword());
    }


}
