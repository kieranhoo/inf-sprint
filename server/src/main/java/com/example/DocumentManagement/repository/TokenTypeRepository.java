package com.example.DocumentManagement.repository;

import com.example.DocumentManagement.entity.TokenTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface TokenTypeRepository extends JpaRepository<TokenTypeEntity, Integer> {
    Optional<TokenTypeEntity> findByTokenTypeName(String tokenTypeName);

    @Query(value = "SELECT * FROM token_type where token_type_name = ?", nativeQuery = true)
    TokenTypeEntity findTypeByTokenTypeName(String tokenTypeName);
}
