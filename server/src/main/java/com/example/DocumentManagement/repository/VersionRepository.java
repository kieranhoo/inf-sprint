package com.example.DocumentManagement.repository;

import com.example.DocumentManagement.entity.VersionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface VersionRepository extends JpaRepository<VersionEntity, Integer> {
    @Query(value = "SELECT * FROM version where document_id = ?1", nativeQuery = true)
    List<VersionEntity> findByDocumentId(int documentId);

    @Query(value = "SELECT * FROM version where id = ?1", nativeQuery = true)
    VersionEntity findById(int id);

    @Query(value = "SELECT * FROM version where document_id = ?1 and current_version = true", nativeQuery = true)
    List<VersionEntity> findByDocumentIdAndCurrentVersionTrue(int documentId);

    @Modifying
    @Transactional
    @Query(value = "UPDATE version SET current_version = false where id = ?", nativeQuery = true)
    void updateCurrentVersion(int id);
}

