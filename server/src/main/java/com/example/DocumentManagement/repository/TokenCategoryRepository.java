package com.example.DocumentManagement.repository;

import com.example.DocumentManagement.entity.TokenCategoryEntity;
import com.example.DocumentManagement.entity.TokenTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface TokenCategoryRepository extends JpaRepository<TokenCategoryEntity, Integer> {
    Optional<TokenCategoryEntity> findByTokenCategoryName(String tokenCategoryName);

    @Query(value = "SELECT * FROM token_category where token_category_name = ?", nativeQuery = true)
    TokenCategoryEntity findCategoryByTokenCategoryName(String tokenCategoryName);
}
