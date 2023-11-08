package com.example.DocumentManagement.repository;

import com.example.DocumentManagement.entity.DocumentEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.util.List;

public interface DocumentRepository extends JpaRepository<DocumentEntity, Integer> {

    @Query(value = "SELECT * FROM document where id = ? and is_deleted = false", nativeQuery = true)
    DocumentEntity findDocumentById(int id);

    @Modifying
    @Transactional
    @Query(value = "UPDATE document SET name = ?, description = ?, create_time = ? where id = ?", nativeQuery = true)
    void updateDocument(String nameDocument, String description, Date createTime, int id);

    @Query(value = "SELECT * FROM document where is_deleted = false", nativeQuery = true)
    Page<DocumentEntity> findAllPageByIsDeletedFalse(Pageable pageable);

    @Query(value = "SELECT * FROM document WHERE department_id = ? AND is_deleted = false", nativeQuery = true)
    List<DocumentEntity> findDocumentsByDepartmentId(int id);

    @Query(value = "SELECT * FROM document WHERE department_id = ? and name REGEXP ?", nativeQuery = true)
    List<DocumentEntity> searchDocuments(int departmentID, String keyword);
}
