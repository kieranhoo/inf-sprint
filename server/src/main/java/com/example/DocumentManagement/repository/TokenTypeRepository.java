package com.example.DocumentManagement.repository;

import com.example.DocumentManagement.entity.TokenTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TokenTypeRepository extends JpaRepository<TokenTypeEntity, Integer> {
    Optional<TokenTypeEntity> findByTokenTypeName(String tokenTypeName);
}
