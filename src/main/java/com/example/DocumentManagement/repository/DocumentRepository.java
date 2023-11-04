package com.example.DocumentManagement.repository;

import com.example.DocumentManagement.entity.DocumentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DocumentRepository extends JpaRepository<DocumentEntity, Integer> {

}
