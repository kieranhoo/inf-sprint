package com.example.DocumentManagement.repository;

import com.example.DocumentManagement.entity.VersionEntity;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface VersionRepository extends JpaRepository<VersionEntity, Integer> {
    List<VersionEntity> findByDocumentId(Long documentId);
    VersionEntity findByDocumentIdAndId(Long documentId, Long versionId);
}

