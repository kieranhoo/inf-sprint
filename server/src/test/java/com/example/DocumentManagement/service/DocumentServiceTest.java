package com.example.DocumentManagement.service;

import com.example.DocumentManagement.entity.DocumentEntity;
import com.example.DocumentManagement.entity.VersionEntity;
import com.example.DocumentManagement.repository.DepartmentRepository;
import com.example.DocumentManagement.repository.DocumentRepository;
import com.example.DocumentManagement.repository.VersionRepository;
import com.example.DocumentManagement.response.DocumentResponse;
import com.example.DocumentManagement.response.ListResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Date;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DocumentServiceTest {
    @InjectMocks
    private DocumentService documentService;
    @Mock
    private DocumentRepository documentRepository;
    @Mock
    private VersionRepository versionRepository;
    @Mock
    private DepartmentRepository departmentRepository;


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
        assertEquals(expectedDocuments.size(), actualDocuments.size());
        assertEquals(expectedDocuments, actualDocuments);
        verify(documentRepository).searchDocuments(departmentId,keyword);
    }
}