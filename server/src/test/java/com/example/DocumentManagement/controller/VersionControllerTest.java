package com.example.DocumentManagement.controller;

import com.example.DocumentManagement.entity.VersionEntity;
import com.example.DocumentManagement.response.ListResponse;
import com.example.DocumentManagement.response.MessageResponse;
import com.example.DocumentManagement.service.VersionService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.sql.Date;
import java.util.Arrays;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class VersionControllerTest {
    @InjectMocks
    VersionController versionController;
    @Mock
    VersionService versionService;

    @Test
    void givenDocumentId_whenGetDocumentVersions_thenReturnListResponse() {
        // Given
        String documentId = "123";
        ListResponse expectedResponse = new ListResponse(
                Arrays.asList(
                        new VersionEntity(14, "https://example.com/version/1", "Version 1.0.0", true, Date.valueOf("2023-11-10"), "Initial version"),
                        new VersionEntity(15, "https://example.com/version/2", "Version 1.0.1", false, Date.valueOf("2023-11-11"), "Update version")
                )
        );
        when(versionService.getAllVersions(documentId)).thenReturn(expectedResponse);
        // When
        ResponseEntity<ListResponse> result = versionController.getDocumentVersions(documentId);
        // Then
        Assertions.assertEquals(HttpStatus.OK, result.getStatusCode());
        Assertions.assertEquals(expectedResponse, result.getBody());
        verify(versionService).getAllVersions(documentId);
    }

    @Test
    void givenId_whenGetOneVersion_thenReturnVersion() {
        // Given
        String id = "123";
        VersionEntity versionEntity = new VersionEntity(14, "https://example.com/version/1", "Version 1.0.0", true, Date.valueOf("2023-11-10"), "Initial version");
        when(versionService.getOneVersion(id)).thenReturn(versionEntity);
        // When
        ResponseEntity<VersionEntity> result = versionController.getOneVersion(id);
        // Then
        Assertions.assertEquals(HttpStatus.OK, result.getStatusCode());
        Assertions.assertEquals(versionEntity, result.getBody());
        verify(versionService).getOneVersion(id);
    }

    @Test
    void givenId_whenGetOneVersion_thenThrowBadRequestException() {
        // Given
        String id = "123";
        MessageResponse expectedResponse = new MessageResponse("Version 1.0.0");
        when(versionService.getLatestVersionByDocumentId(id)).thenReturn(expectedResponse);
        // When
        ResponseEntity<MessageResponse> result = versionController.getLatestVersion(id);
        // Then
        Assertions.assertEquals(HttpStatus.OK, result.getStatusCode());
        Assertions.assertEquals(expectedResponse, result.getBody());
        verify(versionService).getLatestVersionByDocumentId(id);
    }

    @Test
    void testHelloWorld() {
        // Given
        MessageResponse expectedResponse = new MessageResponse("Hello World");
        when(versionService.helloWorld()).thenReturn(expectedResponse);
        // When
        ResponseEntity<MessageResponse> result = versionController.helloWorld();
        // Then
        Assertions.assertEquals(HttpStatus.OK, result.getStatusCode());
        Assertions.assertEquals(expectedResponse, result.getBody());
        verify(versionService).helloWorld();
    }
}
