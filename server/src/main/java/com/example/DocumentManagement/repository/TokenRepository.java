package com.example.DocumentManagement.repository;

import com.example.DocumentManagement.entity.TokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TokenRepository extends JpaRepository<TokenEntity, Integer> {
    List<TokenEntity> findAllByUserIdAndExpiredFalseAndRevokedFalse(Integer userId);

    List<TokenEntity> findAllByUserIdAndTokenCategoryIdAndExpiredFalseAndRevokedFalse(Integer userId, Integer tokenCategoryId);

    Optional<TokenEntity> findByToken(String token);

    Optional<TokenEntity> findByTokenAndAndTokenCategoryIdAndExpiredFalseAndRevokedFalse(String token, Integer id);
}
