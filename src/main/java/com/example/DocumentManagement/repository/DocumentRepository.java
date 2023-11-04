package com.example.DocumentManagement.repository;

import com.example.DocumentManagement.entity.DocumentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface DocumentRepository extends JpaRepository<DocumentEntity, Integer> {   
}
