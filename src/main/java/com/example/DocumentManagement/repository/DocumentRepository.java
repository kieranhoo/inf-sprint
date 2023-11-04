package com.example.DocumentManagement.repository;

import com.example.DocumentManagement.entity.DocumentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DocumentRepository extends JpaRepository<DocumentEntity, Integer> {
}
