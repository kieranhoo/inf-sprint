package com.example.DocumentManagement.controller;

import com.example.DocumentManagement.entity.DepartmentEntity;
import com.example.DocumentManagement.entity.DocumentEntity;
import com.example.DocumentManagement.entity.VersionEntity;
import com.example.DocumentManagement.request.CreateDocumentRequest;
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
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
@ExtendWith(MockitoExtension.class)
public class DocumentControllerTest {

    /*
     * TESTING STRATEGY: BLACK-BOX DECISION TABLE TESTING
     *
     * 1. GetAllDocuments
     * - Input: PageIndex, PageSize
     * - Constrain:
     *   + Valid PageSize: 0 < PageSize < 100 (PageSize<0 -> PageSize=10; PageSize>=100 -> PageSize=100)
     *   + Valid PageIndex: 0 < PageIndex < TotalPage in DB (PageIndex<0 -> PageIndex=1)
     * - States:
     *   a/ Valid PageIndex - Valid PageSize, Valid PageIndex - Invalid PageSize,
     *   Invalid PageIndex - Valid PageSize, Invalid PageIndex - Invalid PageSize
     *   b/ Non-empty PageResponse, PageResponse with Empty DocumentResponse in DB
     * - Output: Response PageResponse (Non-empty, Empty)
     *
     * 2. GetDepartmentById
     * - Input: DepartmentId
     * - States:
     *   a/ Valid DepartmentId, Invalid DepartmentId
     *   b/ Non-empty Department, Zero Department, Empty Department in DB
     * - Output: Response DepartmentEntity (Non-Null, Null)
     *
     * 3. GetAllUsersFromDepartmentId
     * - Input: DepartmentId
     * - States:
     *   a/ Valid DepartmentId, Invalid DepartmentId
     *   b/ Users in Department, No Users in Department, Zero Department in DB
     * - Output: Response List of UsersEntity (Non-empty, Empty, Null)
     * */


    @InjectMocks
    DocumentController underTest;
    @Mock
    DocumentService documentService;
    @Test
    void getDocumentById () {
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
        version1.setCurrentVersion(true);
        version1.setUpdateTime(Date.valueOf("2023-11-09"));
        version1.setNote("Initial version");

        VersionEntity version2 = new VersionEntity();
        version2.setDocumentId(id);
        version2.setUrl("http://example.com/document/1");
        version2.setName("Version 2");
        version2.setCurrentVersion(false);
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
        ResponseEntity<DocumentResponse> result = underTest.getDocumentById(idString);
        // Then
        Assertions.assertEquals(HttpStatus.OK, result.getStatusCode());
        Assertions.assertEquals(documentResponse, result.getBody());
        verify(documentService).getDocumentById(idString);
    }

    @Test
    void getAllDocuments () {
        // Given
        int id = 123;
        DepartmentEntity department = new DepartmentEntity();
        department.setName("IT Department");
        department.setDescription("Handles IT-related tasks");

        VersionEntity version1 = new VersionEntity();
        version1.setDocumentId(id);
        version1.setUrl("http://example.com/document/1");
        version1.setName("Version 1");
        version1.setCurrentVersion(true);
        version1.setUpdateTime(Date.valueOf("2023-11-09"));
        version1.setNote("Initial version");

        VersionEntity version2 = new VersionEntity();
        version2.setDocumentId(id);
        version2.setUrl("http://example.com/document/1");
        version2.setName("Version 2");
        version2.setCurrentVersion(false);
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
        ResponseEntity<ListResponse> result = underTest.getAllDocuments();
        // Then
        Assertions.assertEquals(HttpStatus.OK, result.getStatusCode());
        Assertions.assertEquals(expectedResponse, result.getBody());
        verify(documentService).getAllDocuments();
    }

    @Test
    void givenNewDocumentData_whenCreateDocuments_thenReturnOK() {
        // Given
        CreateDocumentRequest newDocumentData = new CreateDocumentRequest("doc 1", "description for doc 1", "http://example.com/document/1", "Version 1.0.0", "note for doc 1", "1");
        MessageResponse expectedResponse = new MessageResponse("Create SuccessFully!");
        when(documentService.createDocument(newDocumentData)).thenReturn(expectedResponse);

        // When
        ResponseEntity<MessageResponse> responseEntity = underTest.createDocument(newDocumentData);

        // Then
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedResponse, responseEntity.getBody());
        verify(documentService).createDocument(newDocumentData);
    }

    @Test
    void givenDocumentsInDepartment_whenFindAllDocumentsByDepartmentId_thenReturnOK() {
        // Given
        String departmentId = "1";
        DocumentEntity document1 = new DocumentEntity("doc 1", "description for doc 1", Date.valueOf("2023-11-10"), false, null, "1");
        DocumentEntity document2 = new DocumentEntity("doc 2", "description for doc 2", Date.valueOf("2023-11-11"), false, null, "1");
        ListResponse expectedResponse = new ListResponse(Arrays.asList(document1, document2));
        when(documentService.getDocumentsByDepartmentId(departmentId)).thenReturn(expectedResponse);

        // When
        ResponseEntity<ListResponse> responseEntity = underTest.getDocumentsByDepartmentId(departmentId);

        // Then
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedResponse, responseEntity.getBody());
        verify(documentService).getDocumentsByDepartmentId(departmentId);
    }

    @Test
    void givenNoDocumentsInDepartment_whenFindAllDocumentsByDepartmentId_thenReturnOK() {
        // Given
        String departmentId = "1";
        ListResponse expectedResponse = new ListResponse(List.of());
        when(documentService.getDocumentsByDepartmentId(departmentId)).thenReturn(expectedResponse);

        // When
        ResponseEntity<ListResponse> responseEntity = underTest.getDocumentsByDepartmentId(departmentId);

        // Then
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedResponse, responseEntity.getBody());
        verify(documentService).getDocumentsByDepartmentId(departmentId);
    }

    @Test
    void givenNoDepartment_whenFindAllDocumentsByDepartmentId_thenReturnOK() {
        // Given
        String departmentId = "1";
        ListResponse expectedResponse = new ListResponse(null);
        when(documentService.getDocumentsByDepartmentId(departmentId)).thenReturn(expectedResponse);

        // When
        ResponseEntity<?> responseEntity = underTest.getDocumentsByDepartmentId(departmentId);

        // Then
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedResponse, responseEntity.getBody());
        verify(documentService).getDocumentsByDepartmentId(departmentId);
    }
}
