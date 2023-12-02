package com.example.DocumentManagement.repository;

import com.example.DocumentManagement.entity.VersionEntity;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

@SpringBootTest
@ExtendWith(SpringExtension.class)
public class VersionRepositoryTest {

    @Autowired
    private VersionRepository versionRepository;

    @AfterEach
    void tearDown() {
        versionRepository.deleteAll();
    }

    @Test
    void givenVersions_whenFindByDocumentId_thenReturnVersions() {
        // Given
        int documentId = 1;
        VersionEntity version1 = new VersionEntity(1, "https://example.com/version/1", "Version 1.0.0", true, Date.valueOf("2023-11-10"), "Initial version");
        VersionEntity version2 = new VersionEntity(1, "https://example.com/version/2", "Version 1.0.1", false, Date.valueOf("2023-11-11"), "Update version");
        versionRepository.save(version1);
        versionRepository.save(version2);
        // When
        List<VersionEntity> actualVersion = versionRepository.findByDocumentId(documentId);
        // Then
        Assertions.assertNotNull(actualVersion);
        Assertions.assertEquals(2, actualVersion.size());
        Assertions.assertEquals(version1.getName(), actualVersion.get(0).getName());
        Assertions.assertEquals(version2.getName(), actualVersion.get(1).getName());
    }
    @Test
    void givenZeroVersion_whenFindByDocumentId_thenReturnEmptyList() {
        // Given
        int documentId = 1;
        // When
        List<VersionEntity> actualVersion = versionRepository.findByDocumentId(documentId);
        // Then
        Assertions.assertNotNull(actualVersion);
        Assertions.assertEquals(0, actualVersion.size());
    }
    @Test
    void givenVersion_whenFindById_thenReturnVersion() {
        // Given
        VersionEntity version = new VersionEntity(1, "https://example.com/version/1", "Version 1.0.0", true, Date.valueOf("2023-11-10"), "Initial version");
        VersionEntity version2 = new VersionEntity(1, "https://example.com/version/2", "Version 1.0.1", false, Date.valueOf("2023-11-11"), "Update version");

        VersionEntity savedVersion = versionRepository.save(version);
        VersionEntity savedVersion2 = versionRepository.save(version2);
        // When
        Optional<VersionEntity> actualVersion = versionRepository.findById(savedVersion.getId());
        // Then
        Assertions.assertNotNull(actualVersion);
        Assertions.assertEquals(version.getName(), actualVersion.get().getName());

    }
    @Test
    void  givenWrongId_whenFindById_thenReturnNull() {
        // Given
        VersionEntity version = new VersionEntity(1, "https://example.com/version/1", "Version 1.0.0", true, Date.valueOf("2023-11-10"), "Initial version");
        VersionEntity version2 = new VersionEntity(1, "https://example.com/version/2", "Version 1.0.1", false, Date.valueOf("2023-11-11"), "Update version");
        VersionEntity savedVersion = versionRepository.save(version);
        VersionEntity savedVersion2 = versionRepository.save(version2);
        // When
        VersionEntity actualVersion = versionRepository.findById(3);
        // Then
        Assertions.assertNull(actualVersion);
    }

    @Test
    void givenVersions_whenFindByDocumentIdAndCurrentVersionTrue_thenReturnVersions() {
        // Given
        int documentId = 1;
        VersionEntity version1 = new VersionEntity(1, "https://example.com/version/1", "Version 1.0.0", true, Date.valueOf("2023-11-10"), "Initial version");
        VersionEntity version2 = new VersionEntity(1, "https://example.com/version/2", "Version 1.0.1", false, Date.valueOf("2023-11-11"), "Update version");
        versionRepository.save(version1);
        versionRepository.save(version2);
        // When
        List<VersionEntity> actualVersion = versionRepository.findByDocumentIdAndCurrentVersionTrue(documentId);
        // Then
        Assertions.assertNotNull(actualVersion);
        Assertions.assertEquals(1, actualVersion.size());
        Assertions.assertEquals(version1.getName(), actualVersion.get(0).getName());
    }

    @Test
    void givenZeroVersion_whenFindByDocumentIdAndCurrentVersionTrue_thenReturnEmptyList() {
        // Given
        int documentId = 1;
        // When
        List<VersionEntity> actualVersion = versionRepository.findByDocumentIdAndCurrentVersionTrue(documentId);
        // Then
        Assertions.assertNotNull(actualVersion);
        Assertions.assertEquals(0, actualVersion.size());
    }

    @Test
    void givenVersions_whenFindLatestVersionByDocumentId_thenReturnVersion() {
        // Given
        VersionEntity version1 = new VersionEntity(1, "https://example.com/version/1", "Version 1.0.0", true, Date.valueOf("2023-11-10"), "Initial version");
        VersionEntity version2 = new VersionEntity(1, "https://example.com/version/2", "Version 1.0.1", false, Date.valueOf("2023-11-11"), "Update version");
        versionRepository.save(version1);
        versionRepository.save(version2);
        // When
        String actualVersion = versionRepository.findLatestVersionByDocumentId(1);
        // Then
        Assertions.assertNotNull(actualVersion);
        Assertions.assertEquals(version1.getName(), actualVersion);
    }

    @Test
    void givenZeroVersion_whenFindLatestVersionByDocumentId_thenReturnNull() {
        // Given
        int documentId = 1;
        // When
        String actualVersion = versionRepository.findLatestVersionByDocumentId(documentId);
        // Then
        Assertions.assertNull(actualVersion);
    }

    @Test
    void givenVersion_whenUpdateCurrentVersion_thenReturnVersion() {
        // Given
        VersionEntity version1 = new VersionEntity(1, "https://example.com/version/1", "Version 1.0.0", true, Date.valueOf("2023-11-10"), "Initial version");
        VersionEntity savedVersion = versionRepository.save(version1);
        // When
        versionRepository.updateCurrentVersion(savedVersion.getId());
        Optional<VersionEntity> actualVersion = versionRepository.findById(savedVersion.getId());
        // Then
        Assertions.assertNotNull(actualVersion);
        Assertions.assertEquals(false, actualVersion.get().getCurrentVersion());
    }

}
