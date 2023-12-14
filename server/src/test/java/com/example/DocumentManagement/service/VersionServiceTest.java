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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VersionServiceTest {
    @InjectMocks
    private VersionService versionService;
    @Mock
    private VersionRepository versionRepository;

    @Test
    void givenVersions_whenGetAllVersions_thenReturnListResponse() {
        // Given
        String documentIdFromParam = "123";
        int documentId = 123;
        List<VersionEntity> expectedVersions = Arrays.asList(
                new VersionEntity(14, "https://example.com/version/1", "Version 1.0.0", true, Date.valueOf("2023-11-10"), "Initial version"),
                new VersionEntity(15, "https://example.com/version/2", "Version 1.0.1", false, Date.valueOf("2023-11-11"), "Update version")
        );
        when(versionRepository.findByDocumentId(documentId)).thenReturn(expectedVersions);
        // When
        ListResponse result = versionService.getAllVersions(documentIdFromParam);
        // Then
        assertEquals(new ListResponse(expectedVersions), "");
    }

    @Test
    void givenZeroVersion_whenGetAllVersions_thenReturnListResponse() {
        // Given
        String documentIdFromParam = "123";
        int documentId = 123;
        List<VersionEntity> expectedVersions = List.of();
        when(versionRepository.findByDocumentId(documentId)).thenReturn(expectedVersions);
        // When
        ListResponse result = versionService.getAllVersions(documentIdFromParam);
        // Then
        assertEquals(new ListResponse(expectedVersions), result);
    }

    @Test
    void givenInvalidId_whenGetOneVersion_thenThrowBadRequestException() {
        // Given
        String idFromPathVariable = "abc";
        // When
        // Then
        assertThrows(BadRequestException.class, () -> versionService.getOneVersion(idFromPathVariable));
    }


    @Test
    void givenId_whenGetOneVersion_thenReturnVersion() {
        // Given
        String idFromPathVariable = "123";
        int id = 123;
        VersionEntity expectedVersion = new VersionEntity(14, "https://example.com/version/1", "Version 1.0.0", true, Date.valueOf("2023-11-10"), "Initial version");
        when(versionRepository.findById(id)).thenReturn(expectedVersion);
        // When
        VersionEntity result = versionService.getOneVersion(idFromPathVariable);
        // Then
        assertEquals(expectedVersion, result);
        // Verify that the method findById() is called with the correct argument
        verify(versionRepository).findById(id);
    }

    @Test
    void givenId_WhenGetOneVersion_ThenReturnNull() {
        // Given
        String idFromPathVariable = "123";
        int id = 123;
        // When
        when(versionRepository.findById(id)).thenReturn(null);
        // Then
        assertNull(versionService.getOneVersion(idFromPathVariable));
        // Verify that the method findById() is called with the correct argument
        verify(versionRepository).findById(id);
    }

    @Test
        // Given
    void givenId_whenGetLatestVersionByDocumentId_thenReturnMessageResponse() {
        String idFromPathVariable = "123";
        int id = 123;
        String expectedVersion = "Version 1.0.0";
        when(versionRepository.findLatestVersionByDocumentId(id)).thenReturn(expectedVersion);
        // When
        MessageResponse result = versionService.getLatestVersionByDocumentId(idFromPathVariable);
        // Then
        assertEquals(expectedVersion, result.getMessage());
        // Verify that the method findLatestVersionByDocumentId() is called with the correct argument
        verify(versionRepository).findLatestVersionByDocumentId(id);
    }

    @Test
    void givenInvalidId_whenGetLatestVersionByDocumentId_thenThrowBadRequestException() {
        // Given
        String idFromPathVariable = "abc";
        // When
        // Then
        assertThrows(BadRequestException.class, () -> versionService.getLatestVersionByDocumentId(idFromPathVariable));
    }


    @Test
    void testHelloWorld() {
        // Given
        MessageResponse expectedResponse = new MessageResponse("Hello World");
        // When
        MessageResponse result = versionService.helloWorld();
        // Then
        assertEquals(expectedResponse, result);
    }
}
