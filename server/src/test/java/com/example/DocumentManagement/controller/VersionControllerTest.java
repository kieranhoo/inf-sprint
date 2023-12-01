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

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class VersionControllerTest {
    @InjectMocks
    VersionController versionController;
    @Mock
    VersionService versionService;
    @Test
    void getDocumentVersions() {
        String documentId = "123";
        ListResponse expectedResponse = new ListResponse();
        when(versionService.getAllVersions(documentId)).thenReturn(expectedResponse);
        ResponseEntity<ListResponse> result = versionController.getDocumentVersions(documentId);
        verify(versionService).getAllVersions(documentId);
        Assertions.assertEquals(HttpStatus.OK, result.getStatusCode());
        Assertions.assertEquals(expectedResponse, result.getBody());
    }

    @Test
    void getOneVersion() {
        String id = "123";
        VersionEntity versionEntity = new VersionEntity(14, "https://example.com/version/1", "Version 1.0.0", true, Date.valueOf("2023-11-10"), "Initial version");
        when(versionService.getOneVersion(id)).thenReturn(versionEntity);
        ResponseEntity<VersionEntity> result = versionController.getOneVersion(id);
        verify(versionService).getOneVersion(id);
        Assertions.assertEquals(HttpStatus.OK, result.getStatusCode());
        Assertions.assertEquals(versionEntity, result.getBody());
    }

    @Test
    void getLatestVersionByDocumentId() {
        String id = "123";
        MessageResponse expectedResponse = new MessageResponse("Version 1.0.0");
        when(versionService.getLatestVersionByDocumentId(id)).thenReturn(expectedResponse);
        ResponseEntity<MessageResponse> result = versionController.getLatestVersion(id);
        verify(versionService).getLatestVersionByDocumentId(id);
        Assertions.assertEquals(HttpStatus.OK, result.getStatusCode());
        Assertions.assertEquals(expectedResponse, result.getBody());
    }
}
