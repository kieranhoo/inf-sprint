package com.example.DocumentManagement.repository;

import com.example.DocumentManagement.entity.UsersEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UsersRepository extends JpaRepository<UsersEntity, Integer> {
    Optional<UsersEntity> findByUsernameOrEmailAndIsDeletedFalse(String username, String email);

    @Query(value = "SELECT count(id) FROM users  WHERE id = ?1 and is_deleted = false", nativeQuery = true)
    Integer checkUserByUserId(int userId);

    @Query(value = "SELECT * FROM users  WHERE id = ?1 and is_deleted = false", nativeQuery = true)
    UsersEntity findByUserId(int userId);
}
