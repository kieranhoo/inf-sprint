package com.example.DocumentManagement.service;

import com.example.DocumentManagement.entity.VersionEntity;
import com.example.DocumentManagement.exception.BadRequestException;
import com.example.DocumentManagement.repository.VersionRepository;
import com.example.DocumentManagement.response.ListResponse;
import com.example.DocumentManagement.response.MessageResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Date;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VersionServiceTest {
    @InjectMocks
    private VersionService versionService;
    @Mock
    private VersionRepository versionRepository;

    @Test
    void getAllVersions() {
        String documentIdFromParam = "123";
        int documentId = 123;
        List<VersionEntity> expectedVersions = Arrays.asList(
                new VersionEntity(14, "https://example.com/version/1", "Version 1.0.0", true, Date.valueOf("2023-11-10"), "Initial version"),
                new VersionEntity(15, "https://example.com/version/2", "Version 1.0.1", false, Date.valueOf("2023-11-11"), "Update version")
        );
        when(versionRepository.findByDocumentId(documentId)).thenReturn(expectedVersions);
        ListResponse result = versionService.getAllVersions(documentIdFromParam);
    }

    @Test
    void getOneVersion() {
        String idFromPathVariable = "123";
        int id = 123;
        VersionEntity expectedVersion = new VersionEntity(14, "https://example.com/version/1", "Version 1.0.0", true, Date.valueOf("2023-11-10"), "Initial version");
        when(versionRepository.findById(id)).thenReturn(expectedVersion);
        VersionEntity result = versionService.getOneVersion(idFromPathVariable);
        assertEquals(expectedVersion, result);
    }

    @Test
    void getLatestVersionByDocumentId() {
        String idFromPathVariable = "123";
        int id = 123;
        String expectedVersion = "Version 1.0.0";
        when(versionRepository.findLatestVersionByDocumentId(id)).thenReturn(expectedVersion);
        MessageResponse result = versionService.getLatestVersionByDocumentId(idFromPathVariable);
        assertEquals(expectedVersion, result.getMessage());
    }

    @Test
    void checkRequest() {
        String request = "123";
        int expectedRequest = 123;
        int result = versionService.checkRequest(request);
        assertEquals(expectedRequest, result);
    }

    @Test
    void checkRequest_ThrowsBadRequestException() {
        String request = "";
        assertThrows(BadRequestException.class, () -> {
            versionService.checkRequest(request);
        });
    }

}
