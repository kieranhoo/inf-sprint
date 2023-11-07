package com.example.DocumentManagement.repository;

import com.example.DocumentManagement.entity.TokenCategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TokenCategoryRepository extends JpaRepository<TokenCategoryEntity, Integer> {
    Optional<TokenCategoryEntity> findByTokenCategoryName(String tokenCategoryName);
}
