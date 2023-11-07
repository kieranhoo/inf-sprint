package com.example.DocumentManagement.repository;

import com.example.DocumentManagement.entity.UserRoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserRoleRepository extends JpaRepository<UserRoleEntity, Integer> {
    @Query(value = "SELECT * FROM user_role WHERE user_id = ?1", nativeQuery = true)
    List<UserRoleEntity> findByUserId(int id);
}
